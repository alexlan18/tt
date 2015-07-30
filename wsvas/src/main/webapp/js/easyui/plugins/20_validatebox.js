﻿(function($) {
	function init(jq) {
		$(jq).addClass("validatebox-text");
	}
	;
	function destroyBox(jq) {
		var data = $.data(jq, "validatebox");
		data.validating = false;
		var tip = data.tip;
		if (tip) {
			tip.remove();
		}
		$(jq).unbind();
		$(jq).remove();
	}
	;
	function bindEvents(jq) {
		var box = $(jq);
		var validatebox = $.data(jq, "validatebox");
		validatebox.validating = false;
		box.unbind(".validatebox").bind("focus.validatebox", function() {
			validatebox.validating = true;
			validatebox.value = undefined;
			(function() {
				if (validatebox.validating) {
					if (validatebox.value != box.val()) {
						validatebox.value = box.val();
						validate(jq);
					}
					setTimeout(arguments.callee, 200);
				}
			})();
		}).bind("blur.validatebox", function() {
			validatebox.validating = false;
			hideTip(jq);
		}).bind("mouseenter.validatebox", function() {
			if (box.hasClass("validatebox-invalid")) {
				showTip(jq);
			}
		}).bind("mouseleave.validatebox", function() {
			hideTip(jq);
		});
	}
	;
	function showTip(jq) {
		var box = $(jq);
        var options = $.data(jq,"validatebox").options;
		var msg = $.data(jq, "validatebox").message;
		var tip = $.data(jq, "validatebox").tip;
		if (!tip) {
			tip = $("<div class=\"easy_tooltip tooltip-right\">" + "<span class=\"tooltip-content\">" + "</span>" + "<span class=\"tooltip-arrow-outer\">" + "</span>" + "<span class=\"tooltip-arrow\">" + "</span>" + "</div>").appendTo("body");
			$.data(jq, "validatebox").tip = tip;
		}
		tip.find(".tooltip-content").html(msg);
		tip.css({
			display : "block",
			left : box.offset().left + box.outerWidth() + 12 + options.deltaX,
			top : box.offset().top - 1
		});
	}
	;
	function hideTip(jq) {
		var tip = $.data(jq, "validatebox").tip;
		if (tip) {
			tip.remove();
			$.data(jq, "validatebox").tip = null;
		}
	}
	;
	function validate(jq) {
		var opts = $.data(jq, "validatebox").options;
        var tip = $.data(jq, "validatebox").tip;
		var box = $(jq);
		var value = box.val();
        if(opts.combo) {
            value = $(opts.combo).combo("getValue");
        }
		function setTipMessage(msg) {
			$.data(jq, "validatebox").message = msg;
		}
		;
		var disabled = box.attr("disabled");
		if (disabled == true || disabled == "true") {
			return true;
		}
		if (opts.required) {
			if (value == "") {
				box.addClass("validatebox-invalid");
				setTipMessage(opts.missingMessage);
				showTip(jq);
				return false;
			}
		}
		if (opts.validType) {
			var result = /([a-zA-Z_]+)(.*)/.exec(opts.validType);
			var rule = opts.rules[result[1]];
			if (value && rule) {
				var param = eval(result[2]);
				if (!rule["validator"](value, param)) {
					box.addClass("validatebox-invalid");
					var message = rule["message"];
					if (param) {
						for ( var i = 0; i < param.length; i++) {
							message = message.replace(new RegExp("\\{" + i + "\\}", "g"), param[i]);
						}
					}
					setTipMessage(opts.invalidMessage || message);
					showTip(jq);
					return false;
				}
			}
		}
		box.removeClass("validatebox-invalid");
		hideTip(jq);
		return true;
	}

	;
	$.fn.validatebox = function(options, param) {
		if (typeof options == "string") {
			return $.fn.validatebox.methods[options](this, param);
		}
		options = options || {};
		return this.each(function() {
			var state = $.data(this, "validatebox");
			if (state) {
				$.extend(state.options, options);
			} else {
				init(this);
				$.data(this, "validatebox", {
					options : $.extend({}, $.fn.validatebox.defaults, $.fn.validatebox.parseOptions(this), options)
				});
			}
			bindEvents(this);
		});
	};
	$.fn.validatebox.methods = {
		destroy : function(jq) {
			return jq.each(function() {
				destroyBox(this);
			});
		},
		validate : function(jq) {
			return jq.each(function() {
				validate(this);
			});
		},
		isValid : function(jq) {
			return validate(jq[0]);
		}
	};
	$.fn.validatebox.parseOptions = function(target) {
		var t = $(target);
        var options =  $.parser.parseOptions(target, [ "id", "width", "height", "left", "top", "title", "iconCls", "cls", "headerCls", "bodyCls", "tools", "href", "required", "validType", "missingMessage", "invalidMessage"]);
		return $.extend(options, {
			required : (t.attr("required") ? (t.attr("required") == "required" || t.attr("required") == "true" || t.attr("required") == true) : undefined),
			validType : (t.attr("validType") || undefined),
			missingMessage : (t.attr("missingMessage") || undefined),
			invalidMessage : (t.attr("invalidMessage") || undefined)
		});
	};
	$.fn.validatebox.defaults = {
		required : false,
		validType : null,
		missingMessage : "该输入项为必输项",
		invalidMessage : null,
        deltaX : 0,
        combo : null,
		rules : {
			email : {
				validator : function(value) {
					return /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i
							.test(value);
				},
				message : "请输入有效的电子邮件地址"
			},
			url : {
				validator : function(value) {
					return /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i
							.test(value);
				},
				message : "请输入有效的URL地址"
			},
			length : {
				validator : function(value, param) {
					var len = $.trim(value).getByteLength();
					return len >= param[0] && len <= param[1];
				},
				message : "输入内容长度必须介于{0}和{1}之间"
			},
			remote : {
				validator : function(url, param) {
					var data = {};
					data[param[1]] = url;
					var fieldText = $.ajax({
						url : param[0],
						dataType : "json",
						data : data,
						async : false,
						cache : false,
						type : "post"
					}).responseText;
					return fieldText == "true";
				},
				message : "请修正该字段"
			}
		}
	};
})(jQuery);