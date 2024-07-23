
//Adding features to code-editor
var editor=CodeMirror.fromTextArea(document.getElementById("editor"),{
    mode: "text/plain",
    theme: "dracula",
    lineNumbers:true,
    autoCloseBrackets:true,
})

function setEditorValue(value) 
{
    editor.setValue(value);
}

//Update CodeMirror mode based on the selected language
function changeLanguage() {
    var language = document.getElementById("inlineFormSelectPref").value;

    switch (language) {
        case "c":
            editor.setOption("mode", "text/x-csrc");
            break;
        case "cpp":
            editor.setOption("mode", "text/x-c++src");
            break;
        case "java":
            editor.setOption("mode", "text/x-java");
            break;
        case "python3":
            editor.setOption("mode", "text/x-python");
            break;
        default:
            editor.setOption("mode", "text/plain");
    }
}


//when run btn clicked
function runCode(){
    var editorContent = editor.getValue();
    return editorContent; // [OR]   window.myStringValue=editorContent; //sending value globally
}


//Editor width and height
function updateEditorSize() {
    var width = $(window).width();
    var newEditorWidth = 0.525 * width;
    editor.setSize(newEditorWidth, 570);
}

$(window).on('resize', function () {
    updateEditorSize();
});

$(document).ready(function () {
    updateEditorSize();
});
