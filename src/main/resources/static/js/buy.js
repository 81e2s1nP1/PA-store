const phone = document.querySelector(".sdt");	
const address = document.querySelector(".address");
const btnCancel = document.getElementById("btn-cancel");

const regex = /^(0|\+84)(\d{9,10})$/;

let eventKeyup = () => {
	if(regex.test(phone.value)){
			document.getElementById("msg").innerHTML = "";
			return true;
		}else if(phone.value === null || phone.value === "") {
			document.getElementById("msg").innerHTML = "";
			return true;
		}else{
			document.getElementById("msg").innerHTML = "Số điện thoại không hợp lệ !";
			return false;
		}	
}

let eventKeyup_address = () => {
	console.log((address.value).length);
	if((address.value).length == 0) {
			document.getElementById("msg-address").innerHTML = "";
			return true;
		}else if((address.value).length > 10){
			document.getElementById("msg-address").innerHTML = "";
			return true;
		}else{
			document.getElementById("msg-address").innerHTML = "Vui lòng nhập địa chỉ chi tiết !";
			return false;
		}	
}

address.addEventListener('keyup', eventKeyup_address);
phone.addEventListener('keyup', eventKeyup);


document.addEventListener("DOMContentLoaded", function () {
    if (btnCancel) {
        btnCancel.addEventListener("click", function () {
            window.location.href = "/";
        });
    }
});
