import ast

import keras
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from keras import backend as K
from keras.preprocessing.text import Tokenizer
from keras.utils.data_utils import pad_sequences

import preprocess_text
import preprocess_text as prepare_text


n = ["id", "date", "name", "text", "typr", "rep", "rtw", "faw", "stcount", "foll", "frien", "listcount"]
positive_data = pd.read_csv("Data/positive.csv", sep=";", error_bad_lines=False, names=n, usecols=["text"])
negative_data = pd.read_csv("Data/negative.csv", sep=";", error_bad_lines=False, names=n, usecols=["text"])

sample_size = min(positive_data.shape[0], negative_data.shape[0])
full_data = np.concatenate((positive_data["text"].values[:sample_size], negative_data["text"].values[:sample_size]),
                           axis=0)
labels = [1] * sample_size + [0] * sample_size

data = [prepare_text.preprocess(t) for t in full_data]

x_train, x_test, y_train, y_test = train_test_split(data, labels, test_size=0.2, random_state=2)


def precision(y_true, y_pred):
    """Precision metric.

    Only computes a batch-wise average of precision.

    Computes the precision, a metric for multi-label classification of
    how many selected items are relevant.
    """
    true_positives = K.sum(K.round(K.clip(y_true * y_pred, 0, 1)))
    predicted_positives = K.sum(K.round(K.clip(y_pred, 0, 1)))
    precision = true_positives / (predicted_positives + K.epsilon())
    return precision


def recall(y_true, y_pred):
    """Recall metric.

    Only computes a batch-wise average of recall.

    Computes the recall, a metric for multi-label classification of
    how many relevant items are selected.
    """
    true_positives = K.sum(K.round(K.clip(y_true * y_pred, 0, 1)))
    possible_positives = K.sum(K.round(K.clip(y_true, 0, 1)))
    recall = true_positives / (possible_positives + K.epsilon())
    return recall


def f1(y_true, y_pred):
    def recall(y_true, y_pred):
        """Recall metric.

        Only computes a batch-wise average of recall.

        Computes the recall, a metric for multi-label classification of
        how many relevant items are selected.
        """
        true_positives = K.sum(K.round(K.clip(y_true * y_pred, 0, 1)))
        possible_positives = K.sum(K.round(K.clip(y_true, 0, 1)))
        recall = true_positives / (possible_positives + K.epsilon())
        return recall

    def precision(y_true, y_pred):
        """Precision metric.

        Only computes a batch-wise average of precision.

        Computes the precision, a metric for multi-label classification of
        how many selected items are relevant.
        """
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

model = keras.models.load_model("Models/gelu/model", custom_objects={"precision": precision, "recall": recall, "f1": f1})
while(True):

    text = input("Введите строку: ")
    print(text)
    pre_text = {preprocess_text.preprocess(text)}
    seq_text = get_sequense(tokenizer, pre_text)
    predicted = model.predict(seq_text)
    true_predicted = 0
    if predicted < 0.55:
        true_predicted = -1
    else:
        true_predicted = 1
    print(predicted)
# import sys
# import csv
# maxInt = sys.maxsize
#
# while True:
#     # decrease the maxInt value by factor 10
#     # as long as the OverflowError occurs.
#
#     try:
#         csv.field_size_limit(maxInt)
#         break
#     except OverflowError:
#         maxInt = int(maxInt/10)
#
# count_lines = 0
# with open('train_set/dataset2.csv', 'r', encoding='utf-8') as f:
#     count_lines = sum(1 for line in f)
# f.close()
# with open('train_set/dataset2.csv', 'r', encoding='utf-8') as f:
#     reader = csv.reader(f, delimiter=';')
#     with open('results/result_long_text2.csv', 'a', newline='', encoding='utf-8') as wr:
#         writer = csv.writer(wr, delimiter=';')
#         true_predicted = 0
#         percent = 0
#         for row in reader:
#             text_inp = {prepare_text.preprocess(row[0])}
#             test_seq = get_sequense(tokenizer, text_inp)
#             predicted = model.predict(test_seq)
#
#             if predicted < 0.55: true_predicted = -1
#             else: true_predicted = 1
#             writer.writerow([row[0], row[1], true_predicted, predicted])
#             if str(row[1]) != str(true_predicted): percent += 1
#             print(((count_lines - percent) / count_lines) * 100, "%")
#             print("remain: ", count_lines - reader.line_num)
#             # print(row[0], " ", row[1], " ", true_predicted, " ", predicted)
#     wr.close()
# f.close()
# while(True):
#
#     text = input("Введите строку: ")
#     print(text)
#     pre_text = {prepare_text.preprocess(text)}
#     seq_text = get_sequense(tokenizer, pre_text)
#     predicted = model.predict(seq_text)
#     true_predicted = 0
#     if predicted < 0.55:
#         true_predicted = -1
#     else:
#         true_predicted = 1
#     print(predicted)
#
# with open('reviews.txt', 'r', encoding='utf8') as file:
#     for line in file:
#         stroke = line.strip()
#         text_inp = {preprocess_text(stroke)}
#         test_seq = get_sequense(tokenizer, text_inp)
#         predicted = model.predict(test_seq)
#         if 0 < predicted < 0.5:
#             result = "0|" + stroke
#         elif 0.5 < predicted < 1:
#             result = "1|" + stroke
#         new_file = open('result_gelu.txt', 'a+', encoding='utf8')
#         new_file.write(result + "\n")
#         new_file.close()
