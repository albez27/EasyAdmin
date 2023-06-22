<br/>
<p align="center">
  <a href="https://github.com/ShaanCoding/ReadME-Generator">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">EasyAdmin</h3>

  <p align="center">
    Current web-application can solve some problems with moderating your public!
    <br/>
    <br/>
    
  </p>
</p>

![Downloads](https://img.shields.io/github/downloads/ShaanCoding/ReadME-Generator/total) ![Contributors](https://img.shields.io/github/contributors/ShaanCoding/ReadME-Generator?color=dark-green) ![Issues](https://img.shields.io/github/issues/ShaanCoding/ReadME-Generator) ![License](https://img.shields.io/github/license/ShaanCoding/ReadME-Generator) 

## Table Of Contents

* [About the Project](#about-the-project)
* [Sentiment module](#Sentiment-module)
* [Web-module](#getting-started)

## About The Project



This application consists of two parts:
A module for analyzing the tonality of the text
EasyAdmin Web Application

You can implement analysis modules into your applications or use EasyAdmin to moderate Vkontakte

## Sentiment module

Current module realized on python with TensorFlow, Keras, NLTK, pymorphy2 and w2v. For start and usage this module install this requirements. 
The module already has a trained network. For access to API use endpoint sentiment_analysis. This endpoint recieve get and post request, required parameter is "text". 
Response look like {"text" : some text, "score": 1}, where score is emotional mark. 1 - pos, 0 - neg

## EasyAdmin

Web-module realized on Java Spring, just clone repo and open in Intellij IDEA and start project.

## Authors

* **Aleksandr Bezrukov** - *Student* - [Aleksandr Bezrukov](https://vk.com/albez27) - *EasyAdmin*
