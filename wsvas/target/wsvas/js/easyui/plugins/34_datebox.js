(function($) {
	function createBox(target) {
		var state = $.data(target, "datebox");
		var opts = state.options;
		$(target).addClass("datebox-f");
		$(target).combo($.extend({}, opts, {
			onShowPanel : function() {
				state.calendar.calendar("resize");
				opts.onShowPanel.call(target);
			}
		}));
		$(target).combo("textbox").parent().addClass("datebox");
		if (!state.calendar) {
			createCalendar();
		}
		function createCalendar() {
			var panel = $(target).combo("panel");
			state.calendar = $("<div></div>").appendTo(panel).wrap("<div class=\"datebox-calendar-inner\"></div>");
			state.calendar.calendar({
				fit : true,
				border : false,
				onSelect : function(date) {
					var value = opts.formatter(date);
					setValue(target, value);
					$(target).combo("hidePanel");
					opts.onSelect.call(target, date);
				}
			});
			setValue(target, opts.value);
			var button = $("<div class=\"datebox-button\"></div>").appendTo(panel);
			$("<a href=\"javascript:void(0)\" class=\"datebox-current\"></a>").html(opts.currentText).appendTo(button);
			$("<a href=\"javascript:void(0)\" class=\"datebox-close\"></a>").html(opts.closeText).appendTo(button);
			button.find(".datebox-current,.datebox-close").hover(function() {
				$(this).addClass("datebox-button-hover");
			}, function() {
				$(this).removeClass("datebox-button-hover");
			});
			button.find(".datebox-current").click(function() {
				state.calendar.calendar({
					year : new Date().getFullYear(),
					month : new Date().getMonth() + 1,
					current : new Date()
				});
			});
			button.find(".datebox-close").click(function() {
				$(target).combo("hidePanel");
			});
		}
		;
	}
	;
	function doQuery(target, q) {
		setValue(target, q);
	}
	;
	function doEnter(target) {
		var opts = $.data(target, "datebox").options;
		var c = $.data(target, "datebox").calendar;
		var value = opts.formatter(c.calendar("options").current);
		setValue(target, value);
		$(target).combo("hidePanel");
	}
	;
	function setValue(target, value) {
		var state = $.data(target, "datebox");
		var opts = state.options;
		$(target).combo("setValue", value).combo("setText", value);
		state.calendar.calendar("moveTo", opts.parser(value));
	}
	;
	$.fn.datebox = function(options, param) {
		if (typeof options == "string") {
			var method = $.fn.datebox.methods[options];
			if (method) {
				return method(this, param);
			} else {
				return this.combo(options, param);
			}
		}
		options = options || {};
		return this.each(function() {
			var state = $.data(this, "datebox");
			if (state) {
				$.extend(state.options, options);
			} else {
				$.data(this, "datebox", {
					options : $.extend({}, $.fn.datebox.defaults, $.fn.datebox.parseOptions(this), options)
				});
			}
			createBox(this);
		});
	};
	$.fn.datebox.methods = {
		options : function(jq) {
			return $.data(jq[0], "datebox").options;
		},
		calendar : function(jq) {
			return $.data(jq[0], "datebox").calendar;
		},
		setValue : function(jq, value) {
			return jq.each(function() {
				setValue(this, value);
			});
		}
	};
	$.fn.datebox.parseOptions = function(target) {
		var t = $(target);
		return $.extend({}, $.fn.combo.parseOptions(target), {});
	};
	$.fn.datebox.defaults = $.extend({}, $.fn.combo.defaults, {
		panelWidth : 180,
		panelHeight : "auto",
		keyHandler : {
			up : function() {
			},
			down : function() {
			},
			enter : function() {
				doEnter(this);
			},
			query : function(q) {
				doQuery(this, q);
			}
		},
		currentText : "今天",
		closeText : "关闭",
		okText : "确定",
		missingMessage : "该输入项为必输项",
		formatter : function(date) {
			var y = date.getFullYear();
			var m = date.getMonth() + 1;
			var d = date.getDate();
			return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
		},
		parser : function(s) {
			if (!s)
				return new Date();
			var ss = s.split('-');
			var y = parseInt(ss[0], 10);
			var m = parseInt(ss[1], 10);
			var d = parseInt(ss[2], 10);
			if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
				return new Date(y, m - 1, d);
			} else {
				return new Date();
			}
		},
		onSelect : function(date) {
		}
	});
})(jQuery);