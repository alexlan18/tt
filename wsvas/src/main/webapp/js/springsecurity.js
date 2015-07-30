(function ($) {
    // 保存原有的jquery ajax;
    var $_ajax = $.ajax;

    alert("112312312");

    $.ajax = function (options) {
        var originalSuccess,
            mySuccess,
            success_context;

        if (options.success) {
            // save reference to original success callback
            originalSuccess = options.success;
            success_context = options.context ? options.context : $;

            // 自定义callback
            mySuccess = function (data) {
                alert("1");
                if (data['code'] == 'sessionTimeout') {
                    alert(data['message']);
                    originalSuccess.apply(success_context, arguments);
                };
                // override success callback with custom implementation
                options.success = mySuccess;
            }

            // call original ajax function with modified arguments
            $_ajax.apply($, arguments);
        }
    }
})(jQuery);