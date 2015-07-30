(function($) {
	function initTimeSpinner(jq) {
		var opt = $.data(jq, "timespinner").options;
		$(jq).spinner(opt);
		$(jq).unbind(".timespinner");
		$(jq).bind("click.timespinner", function() {
			var start = 0;
			if (this.selectionStart != null) {
				start = this.selectionStart;
			} else {
				if (this.createTextRange) {
					var range = jq.createTextRange();
					var s = document.selection.createRange();
					s.setEndPoint("StartToStart", range);
					start = s.text.length;
				}
			}
			if (start >= 0 && start <= 2) {
				opt.highlight = 0;
			} else {
				if (start >= 3 && start <= 5) {
					opt.highlight = 1;
				} else {
					if (start >= 6 && start <= 8) {
						opt.highlight = 2;
					}
				}
			}
			highlight(jq);
		}).bind("blur.timespinner", function() {
			fixValue(jq);
		});
	}
	;
	function highlight(jq) {
		var opts = $.data(jq, "timespinner").options;
		var start = 0, end = 0;
		if (opts.highlight == 0) {
			start = 0;
			end = 2;
		} else {
			if (opts.highlight == 1) {
				start = 3;
				end = 5;
			} else {
				if (opts.highlight == 2) {
					start = 6;
					end = 8;
				}
			}
		}
		if (jq.selectionStart != null) {
			jq.setSelectionRange(start, end);
		} else {
			if (jq.createTextRange) {
				var range = jq.createTextRange();
				range.collapse();
				range.moveEnd("character", end);
				range.moveStart("character", start);
				range.select();
			}
		}
		$(jq).focus();
	}
	;
	function parseTime(jq, value) {
		var opts = $.data(jq, "timespinner").options;
		if (!value) {
			return null;
		}
		var vv = value.split(opts.separator);
		for ( var i = 0; i < vv.length; i++) {
			if (isNaN(vv[i])) {
				return null;
			}
		}
		while (vv.length < 3) {
			vv.push(0);
		}
		return new Date(1900, 0, 0, vv[0], vv[1], vv[2]);
	}
	;
	function fixValue(jq) {
		var opt = $.data(jq, "timespinner").options;
		var val = $(jq).val();
		var time = parseTime(jq, val);
		if (!time) {
			time = parseTime(jq, opt.value);
		}
		if (!time) {
			opt.value = "";
			$(jq).val("");
			return;
		}
		var minTime = parseTime(jq, opt.min);
		var maxTime = parseTime(jq, opt.max);
		if (minTime && minTime > time) {
			time = minTime;
		}
		if (maxTime && maxTime < time) {
			time = maxTime;
		}
		var tt = [ minTime(time.getHours()), minTime(time.getMinutes()) ];
		if (opt.showSeconds) {
			tt.push(minTime(time.getSeconds()));
		}
		var val = tt.join(opt.separator);
		opt.value = val;
		$(jq).val(val);
		function minTime(value) {
			return (value < 10 ? "0" : "") + value;
		}
		;
	}
	;
	function clickSpinner(jq, down) {
		var opt = $.data(jq, "timespinner").options;
		var val = $(jq).val();
		if (val == "") {
			val = [ 0, 0, 0 ].join(opt.separator);
		}
		var vv = val.split(opt.separator);
		for ( var i = 0; i < vv.length; i++) {
			vv[i] = parseInt(vv[i], 10);
		}
		if (down == true) {
			vv[opt.highlight] -= opt.increment;
		} else {
			vv[opt.highlight] += opt.increment;
		}
		$(jq).val(vv.join(opt.separator));
		fixValue(jq);
		highlight(jq);
	}
	;
	$.fn.timespinner = function(options, param) {
		if (typeof options == "string") {
			var callMethod = $.fn.timespinner.methods[options];
			if (callMethod) {
				return callMethod(this, param);
			} else {
				return this.spinner(options, param);// 调用父类的方法
			}
		}
		options = options || {};
		return this.each(function() {
			var timeSpinner = $.data(this, "timespinner");
			if (timeSpinner) {
				$.extend(timeSpinner.options, options);
			} else {
				$.data(this, "timespinner", {
					options : $.extend({}, $.fn.timespinner.defaults, $.fn.timespinner.parseOptions(this), options)
				});
				initTimeSpinner(this);
			}
		});
	};
	$.fn.timespinner.methods = {
		options : function(jq) {
			var opt = $.data(jq[0], "timespinner").options;
			return $.extend(opt, {
				value : jq.val()
			});
		},
		fixValue : function(jq, value) {
			return jq.each(function() {
				$(this).val(value);
				fixValue(this);
			});
		},
		getHours : function(jq) {
			var opt = $.data(jq[0], "timespinner").options;
			var vv = jq.val().split(opt.separator);
			return parseInt(vv[0], 10);
		},
		getMinutes : function(jq) {
			var opt = $.data(jq[0], "timespinner").options;
			var vv = jq.val().split(opt.separator);
			return parseInt(vv[1], 10);
		},
		getSeconds : function(jq) {
			var opt = $.data(jq[0], "timespinner").options;
			var vv = jq.val().split(opt.separator);
			return parseInt(vv[2], 10) || 0;
		}
	};
	$.fn.timespinner.parseOptions = function(target) {
		var t = $(target);
		return $.extend({}, $.fn.spinner.parseOptions(target), {
			separator : t.attr("separator"),
			showSeconds : (t.attr("showSeconds") ? t.attr("showSeconds") == "true" : undefined),
			highlight : (parseInt(t.attr("highlight")) || undefined)
		});
	};
	$.fn.timespinner.defaults = $.extend({}, $.fn.spinner.defaults, {
		separator : ":",// 年，小时，分钟之间的分离符号
		showSeconds : false,// 是否显示时间秒
		highlight : 0,// 初始化时被选中的时间，0 = hours, 1 = minutes, …
		spin : function(down) {
			clickSpinner(this, down);
		}
	});
})(jQuery);