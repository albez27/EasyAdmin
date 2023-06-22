from nltk.tokenize import sent_tokenize, word_tokenize, RegexpTokenizer
from nltk.corpus import stopwords
from nltk import word_tokenize
from nltk.stem import SnowballStemmer
import nltk
nltk.download('stopwords')
import re
import pymorphy2
morph = pymorphy2.MorphAnalyzer()


def preprocess(text):
    if text is not None:
        # Lowercasing
        text = text.lower()
        # Removal of URLs
        text = re.sub('(user)|(rt)|(url)', '', text)
        text = re.sub('((www\.[^\s]+)|(https?://[^\s]+))', '', text)
        text = re.sub('@[^\s]+', '', text)
        # Removal of HTML Tags
        text = re.sub('<.*?>', '', text)
        # Removing Extra Whitespaces
        text = " ".join(text.split())
        # Tokenization
        result = word_tokenize(text)
        # Spelling Correction
        # Stopwords Removal
        result = remove_stopwords(result)
        # Removing Punctuations
        result = remove_pucnt(result)
        # Stemming
        # result = stemming(result)
        # Lemmatization
        result = lemmatization(result)
        text = to_string(result)

        return text.strip()

def preprocess_(text):
    # Lowercasing
    text = text.lower()
    # Removal of URLs
    text = re.sub('(user)|(rt)|(url)', '', text)
    text = re.sub('((www\.[^\s]+)|(https?://[^\s]+))', '', text)
    text = re.sub('@[^\s]+', '', text)
    # Removal of HTML Tags
    text = re.sub('<.*?>', '', text)
    # Removing Extra Whitespaces
    text = " ".join(text.split())
    return text

def lemmatization(words):
    result = []
    for word in words:
        p = morph.parse(word)[0]
        result.append(p.normal_form)
    return result
# Spelling Correction
# def spell_check(text):
#     result = []
#     spell = SpellChecker(language='ru')
#     for word in text:
#         correct_word = spell.correction(word)
#         if correct_word is not None:
#             result.append(correct_word)
#     return result


# Stopwords Removal
def remove_stopwords(text):
    ru_stopwords = set(stopwords.words("russian"))
    result = []
    for token in text:
        if token not in ru_stopwords:
            result.append(token)
    return result


# Removing Punctuations
def remove_pucnt(text):
    tokenizer = RegexpTokenizer(r"\w+")
    lst = tokenizer.tokenize(' '.join(text))
    return lst


# Stemming
def stemming(text):
    stemmer = SnowballStemmer("russian")
    result = []
    for word in text:
        result.append(stemmer.stem(word))
    return result


def to_string(result):
    return ' '.join(result)

# preprocess("Ну ты не <h1>бери такую же маленькую, как у меня ползать.</h1> Уж точно. Тебе нужно брать большую, которую сам большая есть размеры.")