function webViewToApp(){
    var inputText = document.getElementById('jsText').value;
    WebViewCallbackInterface.webViewToApp(inputText);
}

function appToWebView(getNativeText){
    document.getElementById('nativeText').innerText = getNativeText
    return getNativeText
}