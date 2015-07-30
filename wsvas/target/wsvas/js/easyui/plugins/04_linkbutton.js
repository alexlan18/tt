(function($) {
	function createButton(target) {
		var opts = $.data(target, "linkbutton").options;
		$(target).empty();
		$(target).addClass("l-btn");
		if (opts.id) {
			$(target).attr("id", opts.id);
		} else {
			$.fn.removeProp ? $(target).removeProp("id") : $(target).removeAttr("id");
		}
		if (opts.plain) {
			$(target).addClass("l-btn-plain");
		} else {
			$(target).removeClass("l-btn-plain");
		}
		if (opts.text) {
			$(target).html(opts.text).wrapInner("<span class=\"l-btn-left\">" + "<span class=\"l-btn-text\">" + "</span>" + "</span>");
			if (opts.iconCls) {
				$(target).find(".l-btn-text").addClass(opts.iconCls).css("padding-left", "20px");
			}
		} else {
			$(target).html("&nbsp;").wrapInner("<span class=\"l-btn-left\">" + "<span class=\"l-btn-text\">" + "<span class=\"l-btn-empty\"></span>" + "</span>" + "</span>");
			if (opts.iconCls) {
				$(target).find(".l-btn-empty").addClass(opts.iconCls);
			}
		}
		$(target).unbind(".linkbutton").bind("focus.linkbutton", function() {
			if (!opts.disabled) {
				$(this).find("span.l-btn-text").addClass("l-btn-focus");
			}
		}).bind("blur.linkbutton", function() {
			$(this).find("span.l-btn-text").removeClass("l-btn-focus");
		}).bind("click.linkbutton", function() {
			if (opts.command) {
				setDisabled(target, true);
				$("#" + opts.command).command({
					onExecute : function(data) {
						setDisabled(target, false);
					}
				}).command("execute");
			}
		});
		setDisabled(target, opts.disabled);

		// 是否显示
		if (!opts.visible)
			$(target).hide();
	}
	;

	function setDisabled(target, disabled) {
		var lb = $.data(target, "linkbutton");
		if (disabled) {
			lb.options.disabled = true;
			var href = $(target).attr("href");
			if (href) {
				lb.href = href;
				$(target).attr("href", "javascript:void(0)");
			}
			if (target.onclick) {
				lb.onclick = target.onclick;
				target.onclick = null;
			}
			$(target).addClass("l-btn-disabled");
		} else {
			lb.options.disabled = false;
			if (lb.href) {
				$(target).attr("href", lb.href);
			}
			if (lb.onclick) {
				target.onclick = lb.onclick;
			}
			$(target).removeClass("l-btn-disabled");
		}
	}
	;

	$.fn.linkbutton = function(options, param) {
		if (typeof options == "string") {
			return $.fn.linkbutton.methods[options](this, param);
		}
		options = options || {};
		return this.each(function() {
			var lb = $.data(this, "linkbutton");
			if (lb) {
				$.extend(lb.options, options);
			} else {
				$.data(this, "linkbutton", {
					options : $.extend({}, $.fn.linkbutton.defaults, $.fn.linkbutton.parseOptions(this), options)
				});
				$(this).removeAttr("disabled");
			}
			createButton(this);
		});
	};

	/**
	 * 提供功能方法
	 */
	$.fn.linkbutton.methods = {
		options : function(jq) {
			return $.data(jq[0], "linkbutton").options;
		},

		enable : function(jq) {
			return jq.each(function() {
				setDisabled(this, false);
			});
		},

		disable : function(jq) {
			return jq.each(function() {
				setDisabled(this, true);
			});
		}
	};

	$.fn.linkbutton.parseOptions = function(target) {
		var t = $(target);
		var visible = t.attr("visible");
		if (visible) {
			if (visible == "true")
				visible = true;
			else if (visible == "false")
				visible = false;
			else {
				$.ajax({
					type : "post",
					url : _ContextPath + "/sys/base.do?method=authFunction",
					dataType : "json",
					data : {
						"funcId" : visible
					},
					async : false,
					success : function(json) {
						visible = json;
					}
				});
			}
		} else {
			visible = true;
		}
		return {
			id : t.attr("id"),
			command : t.attr("command"), // add by zhengshj 2012-08-08
			visible : visible, // add by zhengshj 2012-08-09
			disabled : t.attr("disabled") ? true : undefined,
			plain : (t.attr("plain") ? t.attr("plain") == "true" : undefined),
			text : $.trim(t.html()),
			iconCls : (t.attr("icon") || t.attr("iconCls"))
		};
	};

	/**
	 * 默认值
	 */
	$.fn.linkbutton.defaults = {
		id : null,
		disabled : false,
		plain : false,
		text : "",
		iconCls : null
	};
})(jQuery);