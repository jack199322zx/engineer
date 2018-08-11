function showCheck(a) {
	var c = document.getElementById("myCanvas");
	var ctx = c.getContext("2d");
	ctx.clearRect(0, 0, 1000, 1000);
	ctx.font = "80px 'Microsoft Yahei'";
	ctx.fillText(a, 0, 100);
	ctx.fillStyle = "white";
}
var code;
function createCode() {
	code = "";
	var codeLength = 4;
	var selectChar = new Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 'a', 'b', 'c', 'd',
			'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z');
	for (var i = 0; i < codeLength; i++) {
		var charIndex = Math.floor(Math.random() * 60);
		code += selectChar[charIndex];
	}
	if (code.length != codeLength) {
		createCode();
	}
	showCheck(code);
}
function validate() {
	var inputCode = document.getElementById("J_codetext").value.toUpperCase();
	var codeToUp = code.toUpperCase();
	if (inputCode.length <= 0) {
        popup({type:'error',msg:"请输入验证码",delay:1500,bg:true});
		createCode();
		return false;
	} else if (inputCode != codeToUp) {
        popup({type:'error',msg:"验证码错误",delay:1500,bg:true});
		createCode();
		return false;
	} else {
        /* 加载loading */
        $("body").append('<div class="loading"><div class="mask"></div>' +
            '<div id="animationTipBox" style="width: 180px; height: 150px; margin-left: -90px; margin-top: -75px;"><div class="load"><div class="icon_box"><div class="cirBox1"><div class="cir1"></div><div class="cir2"></div><div class="cir3"></div><div class="cir4"></div></div><div class="cirBox2"><div class="cir1"></div><div class="cir2"></div><div class="cir3"></div><div class="cir4"></div></div><div class="cirBox3"><div class="cir1"></div><div class="cir2"></div><div class="cir3"></div><div class="cir4"></div></div></div></div><div class="dec_txt">加载中...</div></div></div>');
        $.getScript("/login/js/login.js", function () {
            login();
        });
        createCode();
		return true;
	}
}