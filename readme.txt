Word Wiz

Word wiz is a assistive android application to help users with reading and writing difficulties 
by using a smart dictionary componet to take images of words and show their defintion and to listen to words and show their defintion

The package name is "mltextreader" due to the fact it was orginally based around using machine learning to read the text but
later deccided on a complete solution assistive technology which would employ different api's to give the best user experience

The application uses Firebase's firestore to hold user data, firebase auth using email, Firebases ml kit for ml vision with the ocr value,
Google's text to speech api

The description popup file is used in both the speech to text and text reader aactivtys to display the word and its defintion using
the Dictionary Request file to make a JSON request for the defintion for oxford dictionary API

The background images of the pages can be  found in the drawables folder

Application needs internet permission actived in the mainifest

Recyler view Adapter is used to show the scroolable saved words section

Espresso and Junit unit tests are used in this project for testing both UI and class logic
