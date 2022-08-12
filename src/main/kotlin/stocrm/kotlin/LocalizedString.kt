package stocrm.kotlin

import com.intellij.openapi.ui.Messages
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.StandardCharsets

class LocalizedString(private var code: String, private var text: String, language: String) {

    private var isExist = true

    private var requestUrl = "https://dev.dev.stocrm.ru/dev/html/translate_plugin/"

    init {
        this.requestUrl += "?LANGUAGE_ID=$language"
        checkCodeAvailability()
    }

    private fun getResponse(params: String): String {
        var response = ""
        val url: URL?
        try {
            url = URL(this.requestUrl + params)

            BufferedReader(InputStreamReader(url.openStream(), StandardCharsets.UTF_8)).use { reader ->
                var line: String?
                reader.readLine().also { line = it }
                response = line.toString()
            }
        } catch (e: Throwable) {
            Messages.showMessageDialog(e.message, "Error", Messages.getInformationIcon())
        }

        return response
    }

    private fun checkCodeAvailability() {
        this.isExist = this.code.isEmpty()
        if (!this.isExist) {
            val response = getResponse("&ACTION=CHECK_STRING_CODE&QUERY=" + this.code)
            try {
                when (response) {
                    "0" -> {
                        this.isExist = false
                    }

                    "1" -> {
                        this.isExist = true
                    }

                    else -> {
                        throw Throwable(response)
                    }
                }
            } catch (e: Throwable) {
                Messages.showMessageDialog(e.message, "Error", Messages.getInformationIcon())
            }
        }
    }

    fun isExist(): Boolean {
        return this.isExist
    }

    fun addCode(): Boolean {
        var isAdded = false
        if (!this.isExist) {
            val response = getResponse("&ACTION=WRITE&STRING_CODE=" + this.code + "&QUERY=" + this.text)
            try {
                when (response) {
                    "1" -> {
                        isAdded = true
                        throw Exception("Успешно добалено")
                    }

                    else -> {
                        throw Throwable(response)
                    }
                }
            } catch (e: Throwable) {
                Messages.showMessageDialog(e.message, "Message", Messages.getInformationIcon())
            }
        }
        return isAdded
    }
}