package lec07

import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class FilePrinter {

    fun readFile() {
        var currentFile = File(".");
        var file = File(currentFile.absolutePath + "/a.txt");
        var reader = BufferedReader(FileReader(file));
        println(reader.readLine())
        reader.close();
    }

    fun readFile2(path: String) {
        BufferedReader(FileReader(path)).use { reader -> println(reader.readLine()) }
    }
}