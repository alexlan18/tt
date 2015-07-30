//日期格式化
Date.prototype.format = function (mask) {
    var d = this;
    var zeroize = function (value, length) {
        if (!length) length = 2;
        value = String(value);
        for (var i = 0, zeros = ''; i < (length - value.length); i++) {
            zeros += '0';
        }
        return zeros + value;
    };
    return mask.replace(/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g, function ($0) {
        switch ($0) {
            case 'd':
                return d.getDate();
            case 'dd':
                return zeroize(d.getDate());
            case 'ddd':
                return ['Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri', 'Sat'][d.getDay()];
            case 'dddd':
                return ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][d.getDay()];
            case 'M':
                return d.getMonth() + 1;
            case 'MM':
                return zeroize(d.getMonth() + 1);
            case 'MMM':
                return ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'][d.getMonth()];
            case 'MMMM':
                return ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'][d.getMonth()];
            case 'yy':
                return String(d.getFullYear()).substr(2);
            case 'yyyy':
                return d.getFullYear();
            case 'h':
                return d.getHours() % 12 || 12;
            case 'hh':
                return zeroize(d.getHours() % 12 || 12);
            case 'H':
                return d.getHours();
            case 'HH':
                return zeroize(d.getHours());
            case 'm':
                return d.getMinutes();
            case 'mm':
                return zeroize(d.getMinutes());
            case 's':
                return d.getSeconds();
            case 'ss':
                return zeroize(d.getSeconds());
            case 'l':
                return zeroize(d.getMilliseconds(), 3);
            case 'L':
                var m = d.getMilliseconds();
                if (m > 99) m = Math.round(m / 10);
                return zeroize(m);
            case 'tt':
                return d.getHours() < 12 ? 'am' : 'pm';
            case 'TT':
                return d.getHours() < 12 ? 'AM' : 'PM';
            case 'Z':
                return d.toUTCString().match(/[A-Z]+$/);
            // Return quoted strings with the surrounding quotes removed     
            default:
                return $0.substr(1, $0.length - 2);
        }
    });
};

/**
 * 通用JS
 *
 * WSVAS
 */

//日志记录
var log = {
    disable: false, //是否开启日志
    logInfos: [],
    getTime: function () {
        return (new Date()).getTime();
    },
    info: function (msg, time) {
        if (this.disable) return;

        //if(this.logInfos.length > 100) this.logInfos = [];
        if (time != undefined) msg += " elapsed  :  " + time + "ms";
        this.logInfos.push(time > 100 ? "<font color=red>" + msg + "</font>" : msg);
    },
    showInfo: function () {
        var dialog = $("#_Dialog_Log");
        if (!dialog.length) {
            dialog = $("<div id=\"_Dialog_Log\" title=\"日志控制台\" modal=\"true\" style=\"width:600px;height:400px;\"></div>").appendTo("body");
        }
        dialog.dialog({
            content: this.logInfos.join("<br/>")
        }).dialog("open");
    }
};


$(window).keydown(function (event) {
    switch (event.keyCode) {
        case 88 :
        {//CTRL+ALT+Z调用日志控制台
            if (event.altKey && event.ctrlKey) {
                log.showInfo();
            }
        }
    }
});

var $document = $(document);

$document.ajaxStart(function () {
    showLoading();
});

$document.ajaxComplete(function (event, request, settings) {
    hideLoading();
    try {
        var jsonResult = $.parseJSON(request.responseText);
        if (jsonResult) {
            if(jsonResult.code != undefined && jsonResult.code == 'sessionTimeout') {
                if (jsonResult.message != undefined && jsonResult.message.length > 0) {
                    $.messager.alert("提示!", jsonResult.message, "error", function() {
                        window.location.href = _ContextPath + "/login/loginPage";
                    });
                }
            } else if(jsonResult.code != undefined && jsonResult.code == 'accessDenied') {
                if (jsonResult.message != undefined && jsonResult.message.length > 0) {
                    $.messager.alert("提示!", jsonResult.message, "error");
                }
            }
        }

        //绑定折叠事件，panel自适应
        $("#mainLayout").layout("panel", "west").panel({
            onCollapse : function() {
                var center = $("#mainLayout").layout("panel", "center");
                $(center).panel("collapse");
                $(center).panel("expand");
            },
            onExpand : function() {
                var center = $("#mainLayout").layout("panel", "center");
                $(center).panel("collapse");
                $(center).panel("expand");
            }
        });

    } catch (e) {

    }
});
// --------【prototype】扩展start-----------------------------------
/**
 * 返回字符串长度 中文字符长度为2
 */
String.prototype.getByteLength = function () {
    var _WJ_ = this.match(/[^\x00-\xff]/ig);
    return this.length + (_WJ_ == null ? 0 : _WJ_.length);
};

/**
 * 数字格式化为金额，默认保留两位数
 * 如:1234.5678 -> 1,234.57
 * @param num
 * @param n
 * @return
 */
function formatNum(num, n) {
    n = n || 2;
    num = num.toFixed(n) + "";
    var intNum = num, decNum = "";
    var pointIndex = num.indexOf(".");
    if (pointIndex >= 0) {
        intNum = num.substring(0, pointIndex);
        decNum = num.substring(pointIndex + 1, num.length);
    }
    var p = /(\d+)(\d{3})/;
    while (p.test(intNum)) {
        intNum = intNum.replace(p, "$1,$2");
    }
    if (decNum) {
        return intNum + "." + decNum;
    } else {
        return intNum;
    }
}

// --------【prototype】扩展end-----------------------------------
var $document = $(document);

function convert_xml_to_string(xmlObject) {
    if (window.ActiveXObject) { // for IE
        return xmlObject.xml;
    } else {
        return (new XMLSerializer()).serializeToString(xmlObject);
    }
}

/************************************
 * 上传文件列表函数
 * refId:对象ID 必输项
 * module : 模块名称 必输项
 * refType:对象类型 ,不超过13字符
 * 例子：uploadFile({
 * 			refId   : xxx
 * 			module  : MKT
 * 			refType : MKTCASE
 * 			width   : 800
 * 			height  : 480
 * 			maximized: true
 * 			exts    :  "xls,exe"  //以逗号隔开
 * 		})
 * *************************************************/
function uploadFile(options) {
    var refId = options.refId;
    var refType = options.refType ? options.refType : "DEFAUTTYPE";
    var title = options.title ? options.title : "上传文件";
    var width = options.width ? options.width : 800;
    var height = options.height ? options.height : 400;
    var maximized = options.maximized ? options.maximized : false;
    var exts = options.exts ? options.exts : "";

    var url = _ContextPath + "/jsp/sys/uploadFile/fileUploadList.jsp?refId=" + refId + "&refType=" + refType + "&exts=" + exts;

    var dlgUpload = $("#_DialogUpload");
    if (!dlgUpload.length) {
        var divStr = '<div id="_DialogUpload"  class="powerui-dialog" modal="true" >';
        divStr += '<iframe id="_uploadFrame" frameborder="0" width="100%" height="100%"></iframe>';
        divStr += "</div>";
        dlgUpload = $(divStr).appendTo('body');
    }
    $("#_uploadFrame").attr("src", url);
    dlgUpload.dialog({
        title: title,
        width: width,
        height: height,
        maximized: maximized
    }).dialog("open");
}

//判断文件是否符合格式
function validateExt(ext, exts) {
    if (exts == "") return true;
    var arrExt = exts.split(",");
    for (var i = 0, len = arrExt.length; i < len; i++) {
        if (ext == arrExt[i]) return true;
    }
    return false;
}
/*************************************上传文件函数***********************************************/

//显示loading
function showLoading() {
    var _loading = $("#_loading");
    if (_loading.length) {
        _loading.show();
    } else {
        $("<div id=\"_loading\" class=\"loading\"><div class=\"loadMsg\"><img src=\"" + _ContextPath + "/images/loading.gif\" /></div></div>").appendTo("body");
    }
}

//隐藏loading
function hideLoading() {
    var _loading = $("#_loading");
    if (_loading.length) {
        _loading.hide();
    }
}





