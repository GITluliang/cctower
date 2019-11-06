//调用mock方法模拟数据(动态栗子)
Mock.mock(
    'http://mockjs', {
        "userName": 'zym', //模拟名称
        "age|1-100": 100, //模拟年龄(1-100)
        "color": "@color", //模拟色值
        "date": "@date('yyyy-MM-dd')", //模拟时间
        "url": "@url()", //模拟url
        "content": "@cparagraph()", //模拟文本

    }
);


//list
Mock.mock(
    'http://list', {

        "longList": [
            {
                "title": "京A12345",
                "t1":"2019-11-1",
                "t2":"2019-11-2",
                "t3":"隆华购物中心-停车场"
            },{
               "title": "沪A12345",
                "t1":"2019-11-1",
                "t2":"2019-11-2",
                "t3":"大运河森林公园-停车场"
            }
        ],
        "listShanghu": [
            {
                "title": "京A12345",
                "t1":"30",
               
            },{
                "title": "沪A12345",
                "t1":"120",
               
            }
        ]      

    }
);


//详情
Mock.mock(
    'http://detail', {
        "title": "京A12345",
        "address":"大运河森林公园-停车场",
        "t1":"字段1",
        "t2":"字段2",
        "t3":"字段3字段3字段3字段3",
        "content": "<p>车主姓名<span>卢亮</span></p>"
                 + "<p>联系方式<span>13477137870</span></p>"
                 + "<p>备注<span style='width:auto;'>这里是备注内容这里是备注内容这里是备注内容这里是备注内容</span></p>"
    }
);
