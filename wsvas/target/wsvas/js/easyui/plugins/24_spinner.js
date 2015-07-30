(function($) {
	function initSpinner(jq) {
		var spinner = $("<span class=\"spinner\">" + "<span class=\"spinner-arrow\">" + "<span class=\"spinner-arrow-up\"></span>" + "<span class=\"spinner-arrow-down\"></span>" + "</span>" + "</span>").insertAfter(jq);
		$(jq).addClass("spinner-text").prependTo(spinner);
		return spinner;
	}
	;
	function reSize(jq, width) {
		var option = $.data(jq, "spinner").options;
		var spinner = $.data(jq, "spinner").spinner;
		if (width) {
			option.width = width;
		}
		var hiddenField = $("<div style=\"display:none\"></div>").insertBefore(spinner);
		spinner.appendTo("body");
		if (isNaN(option.width)) {
			option.width = $(jq).outerWidth();
		}
		var arrowWidth = spinner.find(".spinner-arrow").outerWidth();// 获取箭头宽度
		var width = option.width - arrowWidth;
		if ($.boxModel == true) {
			width -= spinner.outerWidth() - spinner.width();
		}
		$(jq).width(width);
		spinner.insertAfter(hiddenField);
		hiddenField.remove();
	}
	;
	function bindArrow(jq) {
		var options = $.data(jq, "spinner").options;
		var spinner = $.data(jq, "spinner").spinner;
		spinner.find(".spinner-arrow-up,.spinner-arrow-down").unbind(".spinner");
		if (!options.disabled) {
			spinner.find(".spinner-arrow-up").bind("mouseenter.spinner", function() {
				$(this).addClass("spinner-arrow-hover");
			}).bind("mouseleave.spinner", function() {
				$(this).removeClass("spinner-arrow-hover");
			}).bind("click.spinner", function() {
				options.spin.call(jq, false);
				options.onSpinUp.call(jq);
				$(jq).validatebox("validate");
			});
			spinner.find(".spinner-arrow-down").bind("mouseenter.spinner", function() {
				$(this).addClass("spinner-arrow-hover");
			}).bind("mouseleave.spinner", function() {
				$(this).removeClass("spinner-arrow-hover");
			}).bind("click.spinner", function() {
				options.spin.call(jq, true);
				options.onSpinDown.call(jq);
				$(jq).validatebox("validate");
			});
		}
	}
	;
	function setDisableCss(jq, flag) {
		var options = $.data(jq, "spinner").options;
		if (flag) {
			options.disabled = true;
			$(jq).attr("disabled", true);
		} else {
			options.disabled = false;
			$(jq).removeAttr("disabled");
		}
	}
	;
	$.fn.spinner = function(options, param) {
		if (typeof options == "string") {
			var method = $.fn.spinner.methods[options];
			if (method) {
				return method(this, param);
			} else {
				return this.validatebox(options, param);
			}
		}
		options = options || {};
		return this.each(function() {
			var spinner = $.data(this, "spinner");
			if (spinner) {
				$.extend(spinner.options, options);
			} else {
				spinner = $.data(this, "spinner", {
					options : $.extend({}, $.fn.spinner.defaults, $.fn.spinner.parseOptions(this), options),
					spinner : initSpinner(this)
				});
				$(this).removeAttr("disabled");
			}
			$(this).val(spinner.options.value);
			$(this).attr("readonly", !spinner.options.editable);
			setDisableCss(this, spinner.options.disabled);
			reSize(this);
			$(this).validatebox(spinner.options);
			bindArrow(this);
		});
	};
	$.fn.spinner.methods = {
		options : function(jq) {
			var options = $.data(jq[0], "spinner").options;
			return $.extend(options, {
				value : jq.val()
			});
		},
		destroy : function(jq) {
			return jq.each(function() {
				var spinner = $.data(this, "spinner").spinner;
				$(this).validatebox("destroy");
				spinner.remove();
			});
		},
		resize : function(jq, width) {
			return jq.each(function() {
				reSize(this, width);
			});
		},
		enable : function(jq) {
			return jq.each(function() {
				setDisableCss(this, false);
				bindArrow(this);
			});
		},
		disable : function(jq) {
			return jq.each(function() {
				setDisableCss(this, true);
				bindArrow(this);
			});
		},
		getValue : function(jq) {
			return jq.val();
		},
		setValue : function(jq, value) {
			return jq.each(function() {
				var options = $.data(this, "spinner").options;
				options.value = value;
				$(this).val(value);
			});
		},
		clear : function(jq) {
			return jq.each(function() {
				var options = $.data(this, "spinner").options;
				options.value = "";
				$(this).val("");
			});
		}
	};
	$.fn.spinner.parseOptions = function(target) {
		var t = $(target);
		return $.extend({}, $.fn.validatebox.parseOptions(target), {
			width : (parseInt(target.style.width) || undefined),
			value : (t.val() || undefined),
			min : t.attr("min"),
			max : t.attr("max"),
			increment : (parseFloat(t.attr("increment")) || undefined),
			editable : (t.attr("editable") ? t.attr("editable") == "true" : undefined),
			disabled : (t.attr("disabled") ? true : undefined)
		});
	};
	$.fn.spinner.defaults = $.extend({}, $.fn.validatebox.defaults, {
		width : "auto",
		value : "",
		min : null,
		max : null,
		increment : 1,
		editable : true,
		disabled : false,
		spin : function(down) {
		},
		onSpinUp : function() {
		},
		onSpinDown : function() {
		}
	});
})(jQuery);