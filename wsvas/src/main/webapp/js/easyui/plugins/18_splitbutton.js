(function($) {
	function initSplitButton(jq) {
		var opt = $.data(jq, "splitbutton").options;
		$(jq).removeClass("s-btn-active s-btn-plain-active");
		$(jq).linkbutton($.extend({}, opt, {
			text : opt.text + "<span class=\"s-btn-downarrow\">&nbsp;</span>"
		}));
		if (opt.menu) {
			$(opt.menu).menu({
				onShow : function() {
					$(jq).addClass((opt.plain == true) ? "s-btn-plain-active" : "s-btn-active");
				},
				onHide : function() {
					$(jq).removeClass((opt.plain == true) ? "s-btn-plain-active" : "s-btn-active");
				}
			});
		}
		isDisable(jq, opt.disabled);
	}
	;
	function isDisable(jq, flag) {
		var opt = $.data(jq, "splitbutton").options;
		opt.disabled = flag;
		var downArrow = $(jq).find(".s-btn-downarrow");// 下拉箭头
		if (flag) {
			$(jq).linkbutton("disable");
			downArrow.unbind(".splitbutton");
		} else {
			$(jq).linkbutton("enable");
			downArrow.unbind(".splitbutton");
			downArrow.bind("click.splitbutton", function() {
				initMenu();
				return false;
			});
			var timeOutId = null;
			downArrow.bind("mouseenter.splitbutton", function() {
				timeOutId = setTimeout(function() {
					initMenu();
				}, opt.duration);
				return false;
			}).bind("mouseleave.splitbutton", function() {
				if (timeOutId) {
					clearTimeout(timeOutId);// 取消由 setTimeout() 方法设置的 timeout
				}
			});
		}
		function initMenu() {
			if (!opt.menu) {
				return;
			}
			var left = $(jq).offset().left;
			if (left + $(opt.menu).outerWidth() + 5 > $(window).width()) {
				left = $(window).width() - $(opt.menu).outerWidth() - 5;
			}
			$("body>div.menu-top").menu("hide");
			$(opt.menu).menu("show", {
				left : left,
				top : $(jq).offset().top + $(jq).outerHeight()
			});
			$(jq).blur();
		}
		;
	}
	;
	$.fn.splitbutton = function(options, param) {
		if (typeof options == "string") {
			return $.fn.splitbutton.methods[options](this, param);
		}
		options = options || {};
		return this.each(function() {
			var sb = $.data(this, "splitbutton");
			if (sb) {
				$.extend(sb.options, options);
			} else {
				$.data(this, "splitbutton", {
					options : $.extend({}, $.fn.splitbutton.defaults, $.fn.splitbutton.parseOptions(this), options)
				});
				$(this).removeAttr("disabled");
			}
			initSplitButton(this);
		});
	};
	$.fn.splitbutton.methods = {
		options : function(jq) {
			return $.data(jq[0], "splitbutton").options;
		},
		enable : function(jq) {
			return jq.each(function() {
				isDisable(this, false);
			});
		},
		disable : function(jq) {
			return jq.each(function() {
				isDisable(this, true);
			});
		}
	};
	$.fn.splitbutton.parseOptions = function(target) {
		var t = $(target);
		return $.extend({}, $.fn.linkbutton.parseOptions(target), {
			menu : t.attr("menu"),
			duration : t.attr("duration")
		});
	};
	$.fn.splitbutton.defaults = $.extend({}, $.fn.linkbutton.defaults, {
		plain : true,
		menu : null,
		duration : 100
	});
})(jQuery);