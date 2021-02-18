$(function (){
    $("#uploadForm").submit(upload);
});

function upload(){
    //发送AJAX请求之前，将CSRF的令牌设置到消息的请求头中
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e,xhr,options){
        //xhr发送异步请求的核心对象
        xhr.setRequestHeader(header,token);

    });

    var myfile =  document.getElementById("head-image").files;
    var suffix = myfile[0].name.substring(myfile[0].name.lastIndexOf(".")+1).toLowerCase();
    if(suffix!="png"&&suffix!="jgp"&&suffix!="jpeg"&&suffix!="gif"&&suffix!="bmp"){
            alert("图片格式不正确");
            return false;
    }
    if(myfile[0].size>10485760){
        alert("上传图片过大");
        return false;
    }

    $.ajax({
        url: "http://upload-z1.qiniup.com",
        method: "post",
        //不要把表单数据转化为字符串
        processData: false,
        //不设置上传类型 浏览器自动设置
        contentType: false,
        data: new FormData($("#uploadForm")[0]),
        success: function (data) {
            if(data && data.code == 0) {
                // 更新头像访问路径
                $.post(
                    CONTEXT_PATH + "/user/header/url",
                    {"fileName":$("input[name='key']").val()},
                    function (data){
                        data = $.parseJSON(data);
                        if(data.code == 0){
                            window.location.reload();
                        }else{
                            alert(data.msg);
                        }
                    }
                );
            } else {
                alert("上传失败");
            }
        }
    });

    //事件不提交了
    return false;
}