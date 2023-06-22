import csv

from fastapi import FastAPI
import keras
from keras import backend as K
from keras.preprocessing.text import Tokenizer
from keras.utils.data_utils import pad_sequences
import preprocess_text as prepare_text
import vk.auth as vk

app = FastAPI()



@app.get("/")
async def root():
    return {"response": vk.groups()}


# @app.get("/link")
# async def analyze(group_id: str, post_id: str):
#     items = vk.analyze_group(group_id, post_id)
#     result = {}
#     print(len(items))
#     for i in range(len(items)):
#         predicted = predict_comment(items[i]['text'])
#         true_predicted = 0
#         if predicted > 0.55:
#             true_predicted = 1
#         items[i]['class'] = true_predicted
#     return {"result": items}


@app.get("/posts")
async def posts(owner_id: int, count: int, offset: int):
    items = vk.get_posts(owner_id, count, offset)
    return items


@app.get("/screen_name")
async def get_screen_name(groups_ids: str):
    items = vk.get_screen_name(groups_ids)
    return items

@app.get("/getComments")
async def get_comments(owner_id: str, post_id: int):
    items = vk.get_comments_from_selected_posts(owner_id, post_id)
    return items

@app.get("/sentiment_analysis")
async def api_method(text: str):
    predicted = vk.predict_comment(text)
    return {"text": text, "score": predicted}

@app.post("/sentiment_analysis")
async def api_method(text: str):
    predicted = vk.predict_comment(text)
    print(predicted)
    return {"text": text, "score": predicted}
    # return
    # print()
# @app.get("/{comment}")
# async def read_item(comment: str):
#     predicted = predict_comment(comment)
#     print(predicted)
#     if predicted > 0.55:
#         return {"result": "Положительный"}
#     else:
#         return {"result": "Отрицательный"}
