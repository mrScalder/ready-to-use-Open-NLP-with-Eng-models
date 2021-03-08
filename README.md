#Примеры для Apache OpenNPL

## Что это?
Это пример использования OpenNLP для работы с англоязычным текстом.
Обработка текста разделена на несколько этапов:
1) Разделение на предложения - https://opennlp.apache.org/docs/1.9.3/manual/opennlp.html#tools.sentdetect
2) Разделение преложения на ключи - https://opennlp.apache.org/docs/1.9.3/manual/opennlp.html#tools.tokenizer
3) Определение части речи для ключа - https://opennlp.apache.org/docs/1.9.3/manual/opennlp.html#tools.postagger
4) Приведение ключа к начальной форме - https://opennlp.apache.org/docs/1.9.3/manual/opennlp.html#tools.lemmatizer

## Как запустить
### Подготовка моделей
    mvn package -P models
После версии 1.5 перестались выкладываться обученные модели (OpenNLP работает на машинном обучении), поэтому здесь используются модели от версии 1.5.
При этом для приведения к начальной форме (lemmatizer) готовой модели нет, поэтому будет скачан словарь для обучения и запущено обучение (может занять время). 
### Сборка проекта
    mvn package

### Запуск примера
    mvn exec:java