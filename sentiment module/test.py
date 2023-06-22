import json

test1 = [
    {
        "id": 2,
        "from_id": 138826793,
        "date": 1686389593,
        "text": "Комментарий 1",
        "post_id": 1,
        "owner_id": -221060815,
        "parents_stack": [],
        "thread": {
            "count": 1,
            "items": [
                {
                    "id": 3,
                    "from_id": 138826793,
                    "date": 1686506825,
                    "text": "[id138826793|Александр], Тест",
                    "post_id": 1,
                    "owner_id": -221060815,
                    "parents_stack": [
                        2
                    ],
                    "reply_to_user": 138826793,
                    "reply_to_comment": 2
                }
            ],
            "can_post": True,
            "show_reply_button": True,
            "groups_can_post": True
        }
    },
    {
        "id": 4,
        "from_id": 138826793,
        "date": 1686514570,
        "text": "Comment 2",
        "post_id": 1,
        "owner_id": -221060815,
        "parents_stack": [],
        "thread": {
            "count": 0,
            "items": [],
            "can_post": True,
            "show_reply_button": True,
            "groups_can_post": True
        }
    },
    {
        "id": 5,
        "from_id": 196222841,
        "date": 1686515608,
        "text": "Тестовый коммент",
        "post_id": 1,
        "owner_id": -221060815,
        "parents_stack": [],
        "thread": {
            "count": 0,
            "items": [],
            "can_post": True,
            "show_reply_button": True,
            "groups_can_post": True
        }
    }
]

data = {
    "count": 4,
    "items": [
        {
            "id": 2,
            "from_id": 138826793,
            "date": 1686389593,
            "text": "Комментарий 1",
            "post_id": 1,
            "owner_id": -221060815,
            "parents_stack": [],
            "thread": {
                "count": 1,
                "items": [
                    {
                        "id": 3,
                        "from_id": 138826793,
                        "date": 1686506825,
                        "text": "[id138826793|Александр], Тест",
                        "post_id": 1,
                        "owner_id": -221060815,
                        "parents_stack": [
                            2
                        ],
                        "reply_to_user": 138826793,
                        "reply_to_comment": 2
                    },
                    {
                        "id": 3,
                        "from_id": 138826793,
                        "date": 1686506825,
                        "text": "[12312, Тест2",
                        "post_id": 1,
                        "owner_id": -221060815,
                        "parents_stack": [
                            2
                        ],
                        "reply_to_user": 138826793,
                        "reply_to_comment": 2
                    }
                ],
                "can_post": True,
                "show_reply_button": True,
                "groups_can_post": True
            }
        },
        {
            "id": 4,
            "from_id": 138826793,
            "date": 1686514570,
            "text": "Comment 2",
            "post_id": 1,
            "owner_id": -221060815,
            "parents_stack": [],
            "thread": {
                "count": 1,
                "items": [
                    {
                        "id": 3,
                        "from_id": 138826793,
                        "date": 1686506825,
                        "text": "xxxxxxxxxxxxxxxx, Тест",
                        "post_id": 1,
                        "owner_id": -221060815,
                        "parents_stack": [
                            2
                        ],
                        "reply_to_user": 138826793,
                        "reply_to_comment": 2
                    }
                ],
                "can_post": True,
                "show_reply_button": True,
                "groups_can_post": True
            }
        },
        {
            "id": 5,
            "from_id": 196222841,
            "date": 1686515608,
            "text": "Тестовый коммент",
            "post_id": 1,
            "owner_id": -221060815,
            "parents_stack": [],
            "thread": {
                "count": 0,
                "items": [],
                "can_post": True,
                "show_reply_button": True,
                "groups_can_post": True
            }
        }
    ],
    "current_level_count": 3,
    "can_post": True,
    "show_reply_button": True,
    "groups_can_post": True
}

# print(test1)
thread = [{'thread': d["thread"]} for d in data["items"]]
# print(thread)

test = [{'items': d["thread"]["items"]} for d in thread]
print(len(data["items"]))
for i in test:
    if bool(i["items"]):
        for d in i["items"]:
            print(d)
            data["items"].append(d)

print(len(data["items"]))
#     items["items"] = [items[]]
#     print(i["items"])
# print(items)


# selected = [{'id': d["id"], 'commentC': d["comments"]["count"], } for d in data["items"]]
# count = {"commentCount": data["count"]}
# data1 = {}
# data1.setdefault('countComment', data["count"])
# data1.setdefault('items', selected)
# print(data1)
# json_ = json.loads(data)
# json_.join()
# json_.join(json_data)
# json_.join(json.dumps(count))
# print(json_)
# dict1 = json.loads(json_data)
# dict2 = json.loads(json.dumps(count))
# selected.append(dict2)
# # newFile.update(count)
# # newFile.append(selected)
# print(dict1)
# print(dict2)
# print(selected)
