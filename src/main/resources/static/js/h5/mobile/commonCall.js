

//微信屏蔽js分享、复制链接
// $(function(){
//     function onBridgeReady() { 
//         WeixinJSBridge.call('hideOptionMenu'); 
//     } 
    
//     if (typeof WeixinJSBridge == "undefined") { 
//         if (document.addEventListener) { 
//             document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false); 
//         } else if (document.attachEvent) { 
//             document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
//             document.attachEvent('onWeixinJSBridgeReady', onBridgeReady); 
//         } 
//     } else { 
//     onBridgeReady(); 
//     }
// });


// 微信限制
// var ua = navigator.userAgent.toLowerCase();
// var isWeixin = ua.indexOf('micromessenger') != -1;  
// var isAndroid = ua.indexOf('android') != -1;  
// var isIos = (ua.indexOf('iphone') != -1) || (ua.indexOf('ipad') != -1);
// if (!isWeixin) {  
// 	var opened = window.open('https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx75b41ac2d46b1af4&redirect_uri=http%3a%2f%2fwx.wdzgzj.com%2findex.html&response_type=code&connect_redirect=1&scope=snsapi_userinfo&state=123#wechat_redirect', '_self');
// }


//导航栏文字
$(function() {
    var title = $('header h1').text();
    if (title == "") {
        $("title").text("诺泽科技");
    } else {
        $("title").html(title);
    }

});

// 返回顶部
function scrollupBuilder() {
    var scrollup = document.createElement('a');
    scrollup.className = "afix small iconfont icon-xiangshang scrollup afix1";
    scrollup.innerHTML = `<span>顶部</span>`;
    var body = document.getElementsByTagName("body")[0];
    body.appendChild(scrollup);
}
scrollupBuilder();

$(window).scroll(function () {
    if ($(this).scrollTop() > 600) {
        $('.scrollup').fadeIn('slow');
    } else {
        $('.scrollup').fadeOut('slow');
    }
});
$('.scrollup').click(function () {
    $("html, body").animate({scrollTop: 0}, 300);
    return false;
});



//有固定底部标签的
if ($(".fix").length > 0){
    $(".mui-content").css("padding-bottom", "1.5rem");
}


//fix布局出现表单
var windheight = $(window).height();  /*未唤起键盘时当前窗口高度*/
        
$(window).resize(function(){
    var docheight = $(window).height();  /*唤起键盘时当前窗口高度*/        
    if(docheight < windheight){ 
        /*当唤起键盘高度小于未唤起键盘高度时执行*/
        // $(".all-fix").css("display","none");
        $(".all-fix").addClass("active");
    }else{
        $(".all-fix").removeClass("active");
    }    
});



// 自开发弹出层
$(function(){
    $("#yes, .cancel, .close").click(function(){
        $(".popup").hide();
        $("body").css("overflow", "auto");        
    });
});


// 图片缩放：宽度不能低于约78.5%，body不能relative，父层不能hidden
// var obj = new zoom('mask', 'bigimg','smallimg');
// obj.init();



//把实体格式字符串转义成HTML格式的字符串
function escapeStringHTML(str) {
    str = str.replace(/&lt;/g,'<');
    str = str.replace(/&gt;/g,'>');
	str = str.replace(/&#39;/g,"'");
	str = str.replace(/&#62;/g,'>');
	str = str.replace(/&#60;/g,'<');
    return str;
}
//把HTML格式的字符串转义成实体格式字符串
function escapeHTMLString(str) {
    str = str.replace(/</g,'&lt;');
    str = str.replace(/>/g,'&gt;');
    return str;
}


