package com.enation.javashop.android.middleware.utils

import com.enation.javashop.android.middleware.model.GoodsParamParent

/**
 * Created by LDD on 2018/10/17.
 */
class HtmlUtils {

    companion object {
        val get by lazy { HtmlUtils() }
    }

    fun fitImageSize(content : String) :String{
        var result = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\">" +
                     "<html> \n" +
                     "<head> \n" +
                     "<style type=\"text/css\"> \n" +
                     "body {font-size:15px;}\n" +
                     "</style> \n" +
                     "</head> \n" +
                     "<body>" +
                     "<script type='text/javascript'>" +
                     "window.onload = function(){\n" +
                     "var \$img = document.getElementsByTagName('img');\n" +
                     "for(var p in \$img){\n" +
                     " \$img[p].style.width = '100%';\n" +
                     "\$img[p].style.height ='auto'\n" +
                     "}\n" +
                     "}" +
                     "</script> $content " +
                     "</body>" +
                     "</html>"
        return result
    }

    fun buildParamsTable(datas :ArrayList<GoodsParamParent>) :String{
        var result = ""

        datas.forEach { item ->
            result += "<td colspan=\"2\"><strong>${item.name}</strong></td>"
            item.child.forEach({ child ->
                    result += "<tr> <td>${child.name}</td> <td>${child.value}</td> </tr>"
            })
        }

        val format = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\">" +
                "<style>" +
                "    *{margin:0;padding:0;}" +
                "    *{margin: 0}" +
                "    .table-border {" +
                "        border-bottom: solid 1px #e7e7e7;" +
                "        border-left: solid 1px #e7e7e7;" +
                "        min-width: 100%;" +
                "        border-collapse: collapse;" +
                "        border-spacing: 0;" +
                "        word-wrap: break-word;" +
                "        word-break: break-all;" +
                "        font-size: 16px;" +
                "    }" +
                "    .table-border td strong {" +
                "        font-weight: 700;" +
                "        color: #848689;" +
                "    }" +
                "    .table-border td, .table-border th {" +
                "        border-top: solid 1px #e7e7e7;" +
                "        border-right: solid 1px #e7e7e7;" +
                "        padding: 10px;" +
                "    }" +
                "    .table-border td:first-child {" +
                "        width: 65px;" +
                "    }" +
                "    .table-border td {" +
                "        color: #848689;" +
                "        font-size: 12px;" +
                "    }" +
                "    .param-detail{" +
                "        margin:5%;padding:0;" +
                "    }" +
                "</style>" +
                "<html>" +
                "<body>" +
                "<div class=\"param-detail\">" +
                " <table class=\"table-border\" width=\"100%\">" +
                " <tbody>" +
                " $result " +
                " </tbody>" +
                " </table>" +
                "</div>" +
                "</body>" +
                "</html>"

        return format
    }

}