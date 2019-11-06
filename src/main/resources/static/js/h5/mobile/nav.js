

$(function() {
    $(".mui-content").css("padding-bottom", "60px");
})


function navBuilder() {
    var nav = document.createElement('nav');
    nav.className = "nav";
    nav.innerHTML = `

    <a href="home.html">
        <i class="iconfont icon-index-home"></i>首页</a>
    <a href="message.html">
        <i class="iconfont icon-index-message"></i><em></em>消息</a>
    <a href="user.html">
        <i class="iconfont icon-index-user"></i>我的</a>


    `;
    // var body = document.getElementsByTagName("body")[0];
    // body.appendChild(nav);

    var first=document.body.firstChild;//得到页面的第一个元素
    document.body.insertBefore(nav,first);//在得到的第一个元素之前插入 
}
navBuilder();



$(function() {
    $(".mui-content").css("padding-bottom", "1.5rem");
})