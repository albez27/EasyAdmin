import multiprocessing
import gensim
from gensim.models import Word2Vec
import logging


logging.basicConfig(format='%(asctime)s : %(levelname)s : %(message)s', level=logging.INFO)
data = gensim.models.word2vec.LineSentence("Data/tweets.txt")
model = Word2Vec(data, vector_size=200, window=5, min_count=3, workers=multiprocessing.cpu_count())

model.save("Models/tweets_model.bin")