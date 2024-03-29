$().ready(function () {
    validateRule();
});

$.validator.setDefaults({
    submitHandler: function () {
        if($("#serviceCharge").length > 0) {
            if(/^[1-9][0-9]?$/.test($("#serviceCharge").val())){
                update();
            }else {
                parent.layer.alert("服务费必须为1-100之间的正整数")
            }
        }else {
            update();
        }
    }
});

function update() {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/sys/account/update",
        data: $('#signupForm').serialize(),// 你的formid
        async: false,
        error: function (request) {
            parent.layer.alert("Connection error");
        },
        success: function (data) {
            if (data.code == 0) {
                parent.layer.msg("操作成功");
                parent.reLoad();
                var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                parent.layer.close(index);

            } else {
                parent.layer.alert(data.msg)
            }

        }
    });

}

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            name: {
                required: true
            }
        },
        messages: {
            name: {
                required: icon + "请输入名字"
            }
        }
    })
}