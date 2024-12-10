import {_formatPrice} from "./cart2.js";
async function _getFeeship(provinceID, districtID, wardID,quantity,weight,length,width,height,unitship) {
    let url = "province="+provinceID+"&district="+districtID+"&ward="+wardID+"&quantity="+quantity+"&weight="+weight+"&length="+length+"&width="+width+"&height="+height+"&unitShip="+unitship;
    const response = await fetch(contextPathMetaTag.content + "/feeship?"+url, {
        method: "GET",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
        },
    });
    return [response.status, await response.json()];
}
var unit_ship;
let unitshipVal;
async function updateDeliveryMethod() {
    let delivery = document.getElementById('delivery-price')

    unitshipVal = document.getElementById("unit-ship").value;
    if (unitshipVal == "ViettelPost") {
        unitshipVal=1;
    }else if(unitshipVal == "GHN"){
        unitshipVal=0;
    }else{
        document.getElementById("infoShip").innerHTML = "Vui lòng chọn đơn vị vận chuyển";
        delivery.innerHTML = _formatPrice(0);
    }

    //fake data
    let quantity = 1;
    let weight = 100;
    let length = 10;
    let width =20;
    let height = 2;
    let address = $('input[name="address"]:checked').val()
    let province = address.split('-')[0] ,district=address.split('-')[1] ,
        ward=address.split('-')[2];
    // console.log(province, district, ward)
    if(province=="none"||district=="none"||ward=="none"){
        document.getElementById("infoShip").innerHTML = "Vui lòng chọn địa chỉ giao hàng";
        delivery.innerHTML = _formatPrice(0);
    }
    else {
        let [status, data] = await _getFeeship(province, district, ward, quantity, weight, length, width, height, unitshipVal);
        console.log(province, district, ward)
        if (status === 200) {
            data = data.infoShips;
            let s = '';
            data.forEach(function (dt) {
                let currency = parseFloat(dt.GIA_CUOC); // Chuyển đổi giá trị tiền tệ sang số
                currency = currency.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'}); // Định dạng giá trị tiền tệ

                s += '<div class="form-label"><input onchange="updateAplyVoucher()" type="radio" name="infoship" class="m-2" value="' + dt.GIA_CUOC + '" >' + dt.TEN_DICHVU + '- Giá: ' + currency + ' - Thời gian giao: ' + dt.THOI_GIAN + '</div>';
            });
            document.getElementById("infoShip").innerHTML = s;
        }


        function handleRadioChange(event) {
            const selectedValue = parseFloat(event.target.value);
            // Thực hiện hành động bạn muốn dựa trên radio được chọn
            const formattedValue = selectedValue.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
            delivery.setAttribute('data-value', selectedValue);
            delivery.innerHTML = _formatPrice(selectedValue);
            // render();
        }

        let radios = document.getElementsByName('infoship');
        radios.forEach(function(radio) {
            radio.addEventListener('change', handleRadioChange);
        });

    }
}
async function updateDeliveryPrice() {

    unitshipVal= document.getElementById("unit-ship").value;
    unit_ship= document.getElementById("unit-ship");
    unit_ship.addEventListener("change", updateDeliveryMethod
    );

}

document.addEventListener('DOMContentLoaded', updateDeliveryPrice);
export default _getFeeship;