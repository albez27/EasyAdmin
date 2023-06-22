import pandas as pd
import numpy as np
from preprocess_text import preprocess_text
from sklearn.model_selection import train_test_split
from keras import backend as K
from keras.preprocessing.text import Tokenizer
from keras.utils.data_utils import pad_sequences
from gensim.models import Word2Vec
from keras.layers import Input
from keras.layers import Embedding
from keras import optimizers
from keras.layers import Dense, concatenate, Activation, Dropout
from keras.models import Model
from keras.layers.convolutional import Conv1D
from keras.layers.pooling import GlobalMaxPooling1D
from keras.callbacks import ModelCheckpoint
from sklearn.metrics import classification_report


n = ["id", "date", "name", "text", "typr", "rep", "rtw", "faw", "stcount", "foll", "frien", "listcount"]
positive_data = pd.read_csv("Data/positive.csv", sep=";", error_bad_lines=False, names=n, usecols=["text"])
negative_data = pd.read_csv("Data/negative.csv", sep=";", error_bad_lines=False, names=n, usecols=["text"])

sample_size = min(positive_data.shape[0], negative_data.shape[0])
full_data = np.concatenate((positive_data["text"].values[:sample_size], negative_data["text"].values[:sample_size]),
                           axis=0)
labels = [1] * sample_size + [0] * sample_size

data = [preprocess_text(t) for t in full_data]
print(len(data))
x_train, x_test, y_train, y_test = train_test_split(data, labels, test_size=0.2, random_state=2)


def precision(y_true, y_pred):
    true_positives = K.sum(K.round(K.clip(y_true * y_pred, 0, 1)))
    predicted_positives = K.sum(K.round(K.clip(y_pred, 0, 1)))
    precision = true_positives / (predicted_positives + K.epsilon())
    return precision


def recall(y_true, y_pred):
    true_positives = K.sum(K.round(K.clip(y_true * y_pred, 0, 1)))
    possible_positives = K.sum(K.round(K.clip(y_true, 0, 1)))
    recall = true_positives / (possible_positives + K.epsilon())
    return recall


def f1(y_true, y_pred):
    def recall(y_true, y_pred):
        true_positives = K.sum(K.round(K.clip(y_true * y_pred, 0, 1)))
        possible_positives = K.sum(K.round(K.clip(y_true, 0, 1)))
        recall = true_positives / (possible_positives + K.epsilon())
        return recall

    def precision(y_true, y_pred):
        true_positives = K.sum(K.round(K.clip(y_true * y_pred, 0, 1)))
        predicted_positives = K.sum(K.round(K.clip(y_pred, 0, 1)))
        precision = true_positives / (predicted_positives + K.epsilon())
        return precision

    precision = precision(y_true, y_pred)
    recall = recall(y_true, y_pred)
    return 2 * ((precision * recall) / (precision + recall + K.epsilon()))


# HYPER-PARAMS
SENTIMENT_LENGTH = 26
NUM = 10000

def get_sequense(tokenizer, x):
    sequences = tokenizer.texts_to_sequences(x)
    return pad_sequences(sequences, maxlen=SENTIMENT_LENGTH)


tokenizer = Tokenizer(num_words=NUM)
tokenizer.fit_on_texts(x_train)
x_train_seq = get_sequense(tokenizer, x_train)
x_test_seq = get_sequense(tokenizer, x_test)

w2v_model = Word2Vec.load("Models/tweets_model.w2v")
DIM = w2v_model.vector_size
embedding = np.zeros((NUM, DIM))
for word, i in tokenizer.word_index.items():
    if i >= NUM:
        break
    if word in w2v_model.wv.index_to_key:
        embedding[i] = w2v_model.wv[word]

tweet_input = Input(shape=(SENTIMENT_LENGTH, ), dtype="int32")
tweet_encoder = Embedding(NUM, DIM, input_length=SENTIMENT_LENGTH,
                          weights=[embedding], trainable=False)(tweet_input)

branches = []
x = Dropout(0.2)(tweet_encoder)

for size, filters_count in [(2, 10), (3, 10), (4, 10), (5, 10)]:
    for i in range(filters_count):
        branch = Conv1D(filters=1, kernel_size=size, padding='valid', activation='relu')(x)
        branch = GlobalMaxPooling1D()(branch)
        branches.append(branch)
x = concatenate(branches, axis=1)
x = Dropout(0.2)(x)
x = Dense(30, activation='relu')(x)
x = Dense(1)(x)
output = Activation('sigmoid')(x)

model = Model(inputs=[tweet_input], outputs=[output])
model.compile(loss='binary_crossentropy', optimizer='adam', metrics=[precision, recall, f1, 'accuracy'])
model.summary()

# training

x_train_seq = np.array(x_train_seq)
y_train = np.array(y_train)

checkpoint = ModelCheckpoint("Models/frz.hdf5",
                             monitor='val_f1', save_best_only=True, mode='max', period=1)
history = model.fit(x_train_seq, y_train, batch_size=32, epochs=10, validation_split=0.25, callbacks=[checkpoint])

model.load_weights("Models/frz.hdf5")

predicted = np.round(model.predict(x_test_seq))
print(classification_report(y_test, predicted, digits=5))

model.layers[1].trainable = True
adam = optimizers.Adam(lr=0.0001)
model.compile(loss='binary_crossentropy', optimizer=adam, metrics=[precision, recall, f1, 'accuracy'])
model.summary()

checkpoint = ModelCheckpoint("Models/trn.hdf5",
                             monitor='val_f1', save_best_only=True, mode='max', period=1)
history_trainable = model.fit(x_train_seq, y_train, batch_size=32, epochs=5, validation_split=0.25, callbacks=[checkpoint])
model.save("Models/window_5/model")