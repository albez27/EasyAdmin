# client_id = 51650274
# client_secret = "5teqqGaUBwrOpvH9Feew"
# redirect_uri = "http://localhost:8081/code"
# url = "http://oauth.vk.com/authorize"
import json
import numpy as np
import pandas as pd
import vk_api
import ast
import csv
import keras
from keras import backend as K
from keras.preprocessing.text import Tokenizer
from keras.utils.data_utils import pad_sequences
from sklearn.model_selection import train_test_split

import preprocess_text as prepare_text

#access_token=vk1.a.jjc3u5aj_5zHYa0puvraIiIopYfxIxwnacPOKkGKUU-kX3M6F-JaPp_-QwkA_J6c0QA2oBbHVNS8RGi4jpf_L4-vYxWNLPYBSs2brLlbmmJOeHxnZxqni5lmjQ87bZDLboyX-kaA0ePol7mWpNqq2L7YUWOwjn-01lYYLREetxvkfkFUdf_fDdaIUE_L31dG&expires_in=86400&user_id=138826793
token = "vk1.a.B4keFnfLOe3vnGeetQfG_marRU6SgpGPpTRUV9evmp3DuPGYQ2GaLNDRu0FiCTDhpe00yKCBx7rrQzJDnjGcfI_J3NnT5DdJJQakXUbWBT-5iuqEq42OJ5eyxjmm0L2XjLbk7LebkWPknv1sgX_nCp0bfc8_ascgOiy-w46HyKMikgnp06SKNhn5dJBp8LLW"
#access_token=vk1.a.B4keFnfLOe3vnGeetQfG_marRU6SgpGPpTRUV9evmp3DuPGYQ2GaLNDRu0FiCTDhpe00yKCBx7rrQzJDnjGcfI_J3NnT5DdJJQakXUbWBT-5iuqEq42OJ5eyxjmm0L2XjLbk7LebkWPknv1sgX_nCp0bfc8_ascgOiy-w46HyKMikgnp06SKNhn5dJBp8LLW&expires_in=86400&user_id=138826793


def groups():
    vk_session = vk_api.VkApi(token=token)

    vk = vk_session.get_api()
    response = vk.groups.get(user_id=138826793, filter="moder", extended=1)
    return response


def analyze_group(group_id, post_id):
    vk_session = vk_api.VkApi(token=token)

    vk = vk_session.get_api()
    response = vk.wall.getComments(owner_id=group_id, post_id=post_id)

    r = json.dumps(response)
    loaded_r = json.loads(r)
    count = 0
    for i in response['items']:
        count += 1
    base_dir = {}
    for i in range(count):
        base_dir[i] = {'text': response['items'][i]['text'], 'class': []}

    return base_dir


def get_posts(owner_id, count, offset):
    vk_session = vk_api.VkApi(token=token)

    vk = vk_session.get_api()
    response = vk.wall.get(owner_id=owner_id, count=count, offset=offset)

    items = [{'id': d["id"], 'commentCount': d["comments"]["count"], 'likes': d["likes"]["count"], 'date': d["date"],
              'text': d["text"]} for d in response["items"]]
    data = {}
    data.setdefault('countComment', response["count"])
    data.setdefault('items', items)
    return data
    # print(response)


def get_screen_name(group_ids):
    vk_session = vk_api.VkApi(token=token)
    vk = vk_session.get_api()
    response = vk.groups.getById(group_ids=group_ids)
    selected = [{'id': d["id"], 'screen_name': d["screen_name"], "photo_100": d["photo_100"]} for d in response]
    return selected


def get_comments_from_selected_posts(owner_id, post_id):
    vk_session = vk_api.VkApi(token=token)
    vk = vk_session.get_api()
    response = vk.wall.getComments(owner_id=owner_id, post_id=post_id, thread_items_count=10, extented=1)
    thread = [{'thread': d["thread"]} for d in response["items"]]
    # print(thread)

    thread_items = [{'items': d["thread"]["items"]} for d in thread]

    for i in thread_items:
        if bool(i["items"]):
            for d in i["items"]:
                print(d)
                response["items"].append(d)

    comments = [{'from_id': d["from_id"],
                 'id': d["id"],
                 'text': d["text"],
                 'post_id': d["post_id"],
                 'date': d["date"],
                 'score': predict_comment(d['text'])} for d in response["items"]]
    print(len(comments))
    ids = [(d["from_id"]) for d in response["items"]]
    unique_ids = unique(ids)
    data = {}
    data.setdefault('items', comments)
    profiles = vk.users.get(user_ids=unique_ids, fields="photo_100")
    users = [{'id': d["id"], 'userName': d["first_name"] + " " + d['last_name'], 'photo': d["photo_100"]} for d in profiles]
    data.setdefault('profiles', users)
    print(data)
    return data


def unique(list1):
    unique_list = []
    for x in list1:
        if x not in unique_list:
            unique_list.append(x)
    return unique_list


n = ["id", "date", "name", "text", "typr", "rep", "rtw", "faw", "stcount", "foll", "frien", "listcount"]
positive_data = pd.read_csv("Data/positive.csv", sep=";", error_bad_lines=False, names=n, usecols=["text"])
negative_data = pd.read_csv("Data/negative.csv", sep=";", error_bad_lines=False, names=n, usecols=["text"])

sample_size = min(positive_data.shape[0], negative_data.shape[0])
labels = [1] * sample_size + [0] * sample_size

# data = [prepare_text.preprocess(t) for t in full_data]
with open('quick/full_data.txt', 'r', encoding='utf-8') as f:
    data = ast.literal_eval(f.read())
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


SENTIMENT_LENGTH = 26
NUM = 10000


def get_sequense(tokenizer, x):
    sequences = tokenizer.texts_to_sequences(x)
    return pad_sequences(sequences, maxlen=SENTIMENT_LENGTH)


tokenizer = Tokenizer(num_words=NUM)
tokenizer.fit_on_texts(x_train)

model = keras.models.load_model("Models/test_lemma/model", custom_objects={"precision": precision, "recall": recall, "f1": f1})
# data = []
# with open('req/test.csv', 'r', encoding='utf-8') as f:
#     reader = csv.reader(f, delimiter=';')
#     for row in reader:
#         data.append(row[0])
# f.close()
# tokenizer = Tokenizer(num_words=NUM)
# tokenizer.fit_on_texts(data)


def predict_comment(comment):
    text_inp = {prepare_text.preprocess(comment)}
    test_seq = get_sequense(tokenizer, text_inp)
    predicted = model.predict(test_seq)
    print(predicted, " ", comment)
    true_predicted = 0
    if predicted > 0.51:
        true_predicted = 1
    return true_predicted
