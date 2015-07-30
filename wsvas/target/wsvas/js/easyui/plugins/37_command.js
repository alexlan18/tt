(function($) {
	/**
	 * 数据校验
	 */
	function validateRequest(jq){
		var options = $.data(jq, "command").options;
		if(!options.validateRequired) return true;
		
		var commandInfos = options.commandInfos;
		for(var i = 0, len = commandInfos.length; i < len; i++){
			var commandInfo = commandInfos[i];
			var name = commandInfo.name;
			var type = commandInfo.type;
			var submitType = commandInfo.submitType;
			
			if(type == "form" && submitType == "all"){
				var form = $("#"+name);
				if(!form.form("validate")) return false;
			} else if(type == "datagrid"){
				var dg = $("#"+name);
				
				if(submitType == "none") continue;
				// 如果正在编辑， 那儿验证正在编辑的行，如果验证通过则，完成编辑，否则返回。
				var opts = dg.datagrid("options");
				if (opts.editing && opts.editIndex >= 0) {
					if (dg.datagrid("validateRow", opts.editIndex)) {
						dg.datagrid('endEdit', opts.editIndex);
					} else {
						return;
					}
				}
				
				switch(submitType){
					case "all" :
						if(!dg.datagrid("getRows").length){
							$.messager.alert('提示', "提交的表格无数据,请检查!", "info");
							return false;
						}
						break;
					case "changes" :
						if(!dg.datagrid("getChanges").length){
							$.messager.alert('提示', "数据未发生变化!", "info");
							return false;
						}
						break;
					case "current" :
						if(!dg.datagrid("getSelected")){
							$.messager.alert('提示', "请选择记录!", "info");
							return false;
						}
						break;
					case "selected" :
						if(!dg.datagrid("getSelections").length){
							$.messager.alert('提示', "请选择记录!", "info");
							return false;
						}
						break;
					case "none" :
						break;
				}
			} 
		}
		return true;
	}
	/**
	 * 获取提交数据
	 */
	function getRequestData(target){
		var options = $.data(target, "command").options;
		var commandInfos = options.commandInfos;
		
		var requestObj = {};
		for(var i = 0, len = commandInfos.length; i < len; i++){
			var commandInfo = commandInfos[i];
			var name = commandInfo.name;
			var type = commandInfo.type;
			var submitType = commandInfo.submitType;
			//表单数据
			if(type == "form" && submitType == "all"){
				var form = $("#"+name);
				$("[name]",form).each(function(){
					$this = $(this);
					requestObj[$this.attr("name")] = $this.val();
				});
			//表格数据
			} else if(type == "datagrid"){
				var dg = $("#"+name);
				var value;
				switch(submitType){
					case "all" :
						value = JSON.stringify(dg.datagrid("getRows"));
						break;
					case "changes" :
						value = dg.datagrid("getChangestr");
						break;
					case "current" :
						$.extend(requestObj,dg.datagrid("getSelected"));
						break;
					case "selected" :
						value = JSON.stringify(dg.datagrid("getSelections"));
						break;
					case "none" :
						break;
				}
				if(value) requestObj[name] = value;
				//分页参数
				var opts = dg.datagrid("options");
				if (opts.pagination) {
					$.extend(requestObj, {
						page : opts.pageNumber,
						rows : opts.pageSize
					});
				}
				if (opts.sortName) {
					$.extend(requestObj, {
						sort : opts.sortName,
						order : opts.sortOrder
					});
				}
			//自定义参数	
			} else if(type == "param"){
				requestObj[name] = commandInfo.value;
			}
		}
		return requestObj;
	};
	/**
	 * 刷新页面元素
	 */
	function refreshClient(target,operInfo){
		var options = $.data(target, "command").options;
		var commandInfos = options.commandInfos;
		var data = operInfo.data;
		var time0 = log.getTime();
		//处理dropdown
		for(var name in data){
			var dropdown = $("#"+name);
			if(dropdown.length && dropdown.hasClass("powerui-dropdown")){
				dropdown.dropdown("setData",data[name]);
			}
		}
		var time1 = log.getTime();
		log.info("render data dropdown ",time1 - time0);
		for(var i = 0, len = commandInfos.length; i < len; i++){
			var commandInfo = commandInfos[i];
			var name = commandInfo.name;
			var type = commandInfo.type;
			var refreshClient = commandInfo.refreshClient;
			
			if(refreshClient){
				var context = $("#"+name);
				if(type == "form"){
					context.form("load",data);
				} else if(type == "datagrid"){
					context.datagrid("loadData",data[name]);
				}
			}
			var time2 = log.getTime();
			log.info("render data commandInfo (" + name + ") ",time2 - time1);
			time1 = time2;
		}
	};
	function execute(target,params) {
		var startTime = log.getTime();
		var options = $.data(target, "command").options;
		//遮罩效果
		if(options.showProgressBar){
			showLoading();
		}
		$.ajax({
			type : "post",
			url : options.url,
			dataType : "json",
			data : params,
			async : options.async,
			success : function(operInfo) {
				if(operInfo.isOk){
					if(options.showHintInfoOnSuccess){
						$.messager.alert('提示', operInfo.info, "info");
					}
					var endTime = log.getTime(); 
					log.info("execute server and network back-forth transfer ", endTime - startTime);
					//刷新页面元素
					refreshClient(target,operInfo);
					log.info("render data after submit server ", log.getTime() - endTime);
					//回调函数
					if(options.onExecuteSuccess){
						options.onExecuteSuccess.call(target,operInfo.data);
					}
				} else {
					if(options.showHintInfoOnFailure){
						$.messager.alert('提示', operInfo.info, "error");
					}
					
					//回调函数
					if(options.onExecuteFailure){
						options.onExecuteFailure.call(target,operInfo.data);
					}
				}
				//隐藏遮罩效果
				if(options.showProgressBar){
					hideLoading();
				}
				//回调函数
				if(options.onExecute){
					options.onExecute.call(target,operInfo.data);
				}
				var finalTime = log.getTime();
				log.info("-------------command execute finished----------------",finalTime - startTime);
			}
		});
	};
	

	$.fn.command = function(options, param) {
		if (typeof options == "string") {
			var fn = $.fn.command.methods[options];
			if (fn) {
				return fn(this, param);
			}
		}
		return this.each(function() {
			var $this = $.data(this, "command");
			if ($this) {
				$.extend($this.options, options);
			} else {
				$this = $.data(this, "command", {
					options : $.extend({}, $.fn.command.parseOptions(this), options)
				});
			}
		});
	};

	$.fn.command.methods = {
		options : function(jq) {
			return $.data(jq[0], "command").options;
		},
		execute : function(jq,params) {
			var startTime = log.getTime();
			var target = jq[0];
			var options = $.data(target, "command").options;
			log.info("--------" + options.id + " command execute start --------");
			//校验
			if(!validateRequest(target)) {
				if(options.onExecute){
					options.onExecute.call(target);
				}
				return;
			}
			var afterValidateTime = log.getTime();
			log.info("validate request data",afterValidateTime - startTime);
			params = $.extend({},getRequestData(target),params);
			log.info("get request data",log.getTime() - afterValidateTime);
			execute(target,params);
		}
	};

	$.fn.command.parseOptions = function(target) {
		function getCommandInfos(target){
			var commandInfos = [];
			$("commandInfo",target).each(function(){
				var command = $(this);
				var commandInfo = {
						type : command.attr("type"),
						name : command.attr("name"),
						value : command.attr("value"),
						submitType : command.attr("submitType") ? command.attr("submitType") : "all",
						refreshClient : command.attr("refreshClient") ? command.attr("refreshClient") == "true" : false   
				};
				commandInfos.push(commandInfo);
			});
			return commandInfos;
		}
		var $this = $(target);
		return $.extend({}, {
			id : $this.attr("id"),
			url : $this.attr("url"),
			async : $this.attr("async") ? $this.attr("async") == "true" : true,
			showHintInfoOnSuccess : $this.attr("showHintInfoOnSuccess") ? $this.attr("showHintInfoOnSuccess") == "true" : true,
			showHintInfoOnFailure : $this.attr("showHintInfoOnFailure") ? $this.attr("showHintInfoOnFailure") == "true" : true,
			commandInfos : getCommandInfos(target),
			validateRequired : $this.attr("validateRequired") ? $this.attr("validateRequired") == "true" : true,
			showProgressBar : $this.attr("showProgressBar") ? $this.attr("showProgressBar") == "true" : false,
			onExecuteSuccess : $this.attr("onExecuteSuccess") ? eval("(" + $this.attr("onExecuteSuccess") + ")") : undefined,
			onExecuteFailure : $this.attr("onExecuteFailure") ? eval("(" + $this.attr("onExecuteFailure") + ")") : undefined,
			onExecute : $this.attr("onExecute") ? eval("(" + $this.attr("onExecute") + ")") : undefined,
		});
	};

	$.fn.command.defaults = {
		id : null,
		url : null,
		async : false,
		showHintInfoOnSuccess : true,
		showHintInfoOnFailure : true,
		submitForm : null ,
		datagridInfos : [],
		parameters : [],
		validateRequired : true,
		onBeforeExecute : function(data){
			
		},
		onExecuteSuccess : function(data){
			
		},
		onExecuteFailure : function(data){
			
		}
	};
})(jQuery);