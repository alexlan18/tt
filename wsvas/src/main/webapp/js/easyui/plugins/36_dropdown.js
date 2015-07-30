(function($) {
	function loadData(jq) {
		var options = $.data(jq, "dropdown").options;
		if (options.url) {
			$.ajax({
				type : "post",
				url : options.url,
				dataType : "json",
				data : options.paramData,
				async : false,
				success : function(json) {
					$.data(jq, "dropdown").data = json;
				}
			});
		} else if (options.data) {
			$.data(jq, "dropdown").data = options.data;
		} else {
			$.data(jq, "dropdown").data = [];
		}
	}
	;

	$.fn.dropdown = function(options, param) {
		if (typeof options == "string") {
			var fn = $.fn.dropdown.methods[options];
			if (fn) {
				return fn(this, param);
			}
		}
		return this.each(function() {
			var $this = $.data(this, "dropdown");
			if ($this) {
				$.extend($this.options, options);
			} else {
				$this = $.data(this, "dropdown", {
					options : $.extend({}, $.fn.combobox.defaults, $.fn.combobox.parseOptions(this), options)
				});
			}
			loadData(this);
		});
	};

	$.fn.dropdown.methods = {
		options : function(jq) {
			return $.data(jq[0], "dropdown").options;
		},
		setData : function(jq,data) {
			$.data(jq[0], "dropdown").data = data;
			//重置combobox
			var combobox = $(".powerui-combobox[dropdown=\"#"+jq[0].id+"\"]"); 
			combobox.combobox("loadData",data);
		},
		// 获取数据
		getData : function(jq) {
			return $.data(jq[0], "dropdown").data;
		},
		// 获取数据的文本值
		getText : function(jq, value) {
			var options = $.data(jq[0], "dropdown").options;
			var valueField = options.valueField;
			var textField = options.textField;
			var data = $.data(jq[0], "dropdown").data;
			if (data && data.length) {
				for ( var i = 0, len = data.length; i < len; i++) {
					if (data[i][valueField] == value) {
						return data[i][textField];
					}
				}
			}
			return value;
		}
	};

	$.fn.dropdown.parseOptions = function(target) {
		var $this = $(target);
		return $.extend({}, {
			valueField : $this.attr("valueField"),
			textField : $this.attr("textField"),
			url : $this.attr("url")
		});
	};

	$.fn.dropdown.defaults = {
		valueField : null,
		textField : null,
		url : null,
		data : null,
		paramData : null
	};
})(jQuery);