import firebase from 'firebase/compat/app';

const FirebaseProvider = ({ children }) => {


    const firebaseConfig = {
        apiKey: "AIzaSyBBF6IoDFGik8XFGvTuqqETAqoV-h1wRt0",
        authDomain: "lojister-app.firebaseapp.com",
        projectId: "lojister-app",
        storageBucket: "lojister-app.appspot.com",
        messagingSenderId: "802995063734",
        appId: "1:802995063734:web:0cdb3a29e3402869cb0f17",
        measurementId: "G-ZSBHXJ03G5"
    };

    // const firebaseConfig = {
    //     apiKey: "AIzaSyBtUJcCa075UhubKXIFT0MG3pBfqtX37es",
    //     authDomain: "lojister-e69f6.firebaseapp.com",
    //     projectId: "lojister-e69f6",
    //     storageBucket: "lojister-e69f6.appspot.com",
    //     messagingSenderId: "768557683966",
    //     appId: "1:768557683966:web:364ca01b3c41361805c1af",
    //     measurementId: "G-CEWZQCNB3B"
    //   };

    try {
        firebase.initializeApp(firebaseConfig);
    } catch (error) {

    }
    return children
}

export default FirebaseProvider;