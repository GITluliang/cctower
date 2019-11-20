var prefix = "/sys/car/long";
$().ready(function () {
    validateRule();
});

$.validator.setDefaults({
    submitHandler: function () {
        save();
    }
});

function save() {
    $.ajax({
        cache: true,
        type: "POST",
        url: prefix + "/save",
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
        errorPlacement: function (error, element) {
            error.appendTo(element.parent().parent());
        },
        rules: {
            parkingId: {
                required: true
            },
            number: {
                required: true
            },
            beginDate: {
                required: true
            },
            endDate: {
                required: true
            }
        },
        messages: {
            parkingId: {
                required: icon + "请选择停车场"
            },
            number: {
                required: icon + "请输入车牌号"
            },
            beginDate: {
                required: icon + "请选择生效日期"
            },
            endDate: {
                required: icon + "请选择失效日期"
            }
        }
    })
}