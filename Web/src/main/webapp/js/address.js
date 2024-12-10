// import {loadAddress} from "./cart";
// import {_formatPrice} from "./cart";
$(document).ready(function (){
    loadAddress()
})
const contextPathMetaTag = document.querySelector("meta[name='contextPath']");
const currentUserIdMetaTag = document.querySelector("meta[name='currentUserId']");
var province;
var district;
var ward;

async function _getProvice() {
    const response = await fetch(contextPathMetaTag.content + "/address", {
        method: "GET",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
        },
    });

    return [response.status, await response.json()];
}

async function _getDistrictByProvince(name) {
    const response = await fetch(contextPathMetaTag.content + "/address?provinceName=" + name, {
        method: "GET",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
        },
    });
    return [response.status, await response.json()];
}

async function _getWardByDistrict(name) {
    const response = await fetch(contextPathMetaTag.content + "/address?districtName=" + name, {
        method: "GET",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
        },
    });
    return [response.status, await response.json()];
}

async function _getUserAddress() {
    const response = await fetch(contextPathMetaTag.content + "/userAddress", {
        method: "GET",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
        },
    });
    return [response.status, await response];
}

let unit_ship;

document.addEventListener('DOMContentLoaded', async function () {
    unit_ship = document.getElementById("unit-ship");
    // var selectProvince = document.getElementById('selectProvince');
    // async function getAddressData() {
    //     try {
    //         const [status, data] = await _getUserAddress();
    //         if (status === 200) {
    //             for(var value in data){
    //                 data[value].forEach(function (dt){
    //                     let option = '<option value="'+dt.province.name+'-'+ dt.district.name
    //                         +'-'+ dt.ward.name +'</option>';
    //                     selectProvince.innerHTML += option;
    //                     console.log(option)
    //                 })
    //             }
    //         } else {
    //             console.log("Yêu cầu không thành công. Mã trạng thái: " + status);
    //         }
    //     } catch (error) {
    //         console.log("Đã xảy ra lỗi:", error);
    //     }
    // }

    district = document.getElementById("selectDistrict");
    ward = document.getElementById("selectWard");
    province = document.getElementById("selectProvince");
    province.addEventListener("change", async function () {

        // Lấy phần tử option được chọn
        let selectedOption = province.options[province.selectedIndex];
        // Lấy giá trị của option được chọn
        let selectedValue = selectedOption.value;
        // In giá trị ra console hoặc thực hiện các thao tác khác
        let unitshipVal = "none";
        await selectDistrict();
        selectWard()
        if (unit_ship != null) {
            var event = new Event('change');
            unit_ship.dispatchEvent(event);
        }
    });
    district.addEventListener("change", async function () {

        // Lấy phần tử option được chọn
        let selectedOption = district.options[district.selectedIndex];
        // Lấy giá trị của option được chọn
        let selectedValue = selectedOption.value;
        // In giá trị ra console hoặc thực hiện các thao tác khác
        await selectWard();
        if (unit_ship != null) {
            var event = new Event('change');
            unit_ship.dispatchEvent(event);
        }
    });
    ward.addEventListener("change", async function () {
        if (unit_ship != null) {
            var event = new Event('change');
            unit_ship.dispatchEvent(event);
        }
    });
    // getAddressData();
    await selectProvince();
    await selectDistrict();
    await selectWard()
});

async function selectDistrict() {


// Gọi sự kiện change trên phần tử select


    await _getDistrictByProvince(document.getElementById('selectProvince').value).then(([status, data]) => {
            if (status === 200) {


                // Thêm option mới vào trong select

                data = data.districts;
                let option = '<option value="none" selected>Chọn quận/huyện</option>';
                data.forEach(function (dt) {
                    // if (dt.DISTRICT_NAME == document.getElementById('selectDistrict').value) {
                    //
                    //     option += '<option selected value="' + dt.DISTRICT_NAME + '">' + dt.DISTRICT_NAME + '</option>';
                    // } else {
                    option += '<option value="' + dt.DISTRICT_NAME + '">' + dt.DISTRICT_NAME + '</option>';
                    // }

                });
                district.innerHTML = option;
            }
        }
    )
    // if(unit_ship!=null) {
    //     var event = new Event('change');
    //     unit_ship.dispatchEvent(event);
    // }
}
function changeUnitShip(){
    $('#delivery-price').text('---');
    $('#delivery-price').attr('data-value', 0);
    $('#voucher-ship').text(0);
    $('#voucher-ship').attr('data-value', 0);
    updateToTalPrice()
}
async function selectProvince() {

    await _getProvice().then(([status, data]) => {
            if (status === 200) {


                data = data.provinces;

                // Thêm option mới vào trong select
                let option = '<option value="none"  selected>Chọn tỉnh/thành phố</option>';


                data.forEach(function (dt) {
                    // if (dt.PROVINCE_NAME == document.getElementById('selectProvince').value) {
                    //     option += '<option selected value="' + dt.PROVINCE_NAME + '">' + dt.PROVINCE_NAME + '</option>';
                    // } else {
                    option += '<option value="' + dt.PROVINCE_NAME + '">' + dt.PROVINCE_NAME + '</option>';
                    // }


                    // Thêm option mới vào trong select
                });
                province.innerHTML = option;
            }
        }
    )
    // if(unit_ship!=null) {
    //     var event = new Event('change');
    //     unit_ship.dispatchEvent(event);
    // }
}

async function selectWard() {

    await _getWardByDistrict(document.getElementById("selectDistrict").value).then(([status, data]) => {
            if (status === 200) {


                data = data.wards;

                // Thêm option mới vào trong select
                let option = '<option value="none" selected>Chọn phường/xã</option>';
                data.forEach(function (dt) {
                    // if (dt.WARDS_NAME == document.getElementById('selectWard').value) {
                    //     option += '<option selected value="' + dt.WARDS_NAME + '">' + dt.WARDS_NAME + '</option>';
                    // } else {
                    option += '<option value="' + dt.WARDS_NAME + '">' + dt.WARDS_NAME + '</option>';
                    // }

                });
                ward.innerHTML = option;
            }
        }
    )

}

function submitAddAddress() {
    let province = $('#selectProvince').val();
    let district = $('#selectDistrict').val();
    let ward = $('#selectWard').val();
    let house_number = $('#house_number').val()
    let province_error = $('#province-error')
    let district_error = $('#district-error')
    let ward_error = $('#ward-error')
    let house_number_error = $('#house_number-error')

    province_error.text("");
    district_error.text("");
    ward_error.text("");
    house_number_error.text("")

    if ($('#selectProvince').val() == 'none') {
        province_error.text('Vui lòng chọn tỉnh/thành phố!');
    }
    if ($('#selectDistrict').val() == 'none') {
        console.log(district, ward)
        district_error.text('Vui lòng chọn quận/huyện!')
    }
    if ($('#selectWard').val() == 'none') {
        console.log(district, ward)
        ward_error.text('Vui lòng chọn phường/xã!')
    }
    if (house_number.length == 0) {
        house_number_error.text('Vui lòng điền số nhà!')
    }
    if (province_error.text().length == 0 && district_error.text().length == 0
        && ward_error.text().length == 0 && house_number_error.text().length == 0) {
        console.log(province, district, ward, house_number)
        addAddress(province, district, ward, house_number)
    }

}

function addAddress(province, district, ward, house_number) {
    $.ajax({
            url: '/address',
            type: 'post',
            data: {
                'type': 'add',
                'province': province,
                'district': district,
                'ward': ward,
                'house_number': house_number
            },
            success: function (response) {
                loadAddress()
                alert(response.msg)
            },
            error: function (response) {
                alert(response.responseJSON.error)

            }
        }
    )
}

function loadAddress() {

    $.ajax({
            url: '/address',
            type: 'post',
            data: {
                'type': 'get'
            },
            success: function (response) {
                $('#address_content').empty()
                var data = response.data
                var addresses = "";
                for (let address in data) {
                    addresses += data[address].address
                }
                $('#address_content').append(addresses)
            },
            error: function (response) {
                alert(response.responseJSON.error)

            }
        }
    )
}

function setShipInfo() {
    changeUnitShip()
    let unitshipVal = $("#unit-ship").val();
    let unit_ship = $("#unit-ship");
    let delivery = $('#delivery-price')
    if (unitshipVal != 'none') {
        if (unitshipVal == "ViettelPost") {
            unitshipVal = 1;
        } else if (unitshipVal == "GHN") {
            unitshipVal = 0;
        }
        let quantity = 1;
        let weight = 100;
        let length = 10;
        let width = 20;
        let height = 2;
        let address = $('input[name="address"]:checked').val()
        let province = address.split('-')[0], district = address.split('-')[1],
            ward = address.split('-')[2];
        let url = "province=" + province + "&district=" + district +
            "&ward=" + ward + "&quantity=" + quantity + "&weight=" + weight +
            "&length=" + length + "&width=" + width + "&height=" + height + "&unitShip=" + unitshipVal;
        $.ajax({
            url: '/feeship?' + url,
            type: 'GET',
            success: function (response) {
                console.log(response.infoShips)
                let data = response.infoShips;
                let s = '';
                data.forEach(function (dt) {
                    // Chuyển đổi giá trị tiền tệ sang số
                    let currency = parseFloat(dt.GIA_CUOC);
                    // Định dạng giá trị tiền tệ
                    currency = currency.toLocaleString('vi-VN',
                        {style: 'currency', currency: 'VND'});
                    s += '<div class="form-label"><input onchange="updateAplyVoucher()" ' +
                        'type="radio" name="infoship" class="m-2" value="' + dt.GIA_CUOC + '" ' +
                        '>' + dt.TEN_DICHVU + '- Giá: ' + currency + ' - Thời gian giao: ' + dt.THOI_GIAN + '</div>';
                    $('#infoShip').empty()
                    $('#infoShip').append(s)

                });
                let radios = $('input[name="infoship"]');
                radios = Array.from(radios);
                radios.forEach(function(radio) {
                    radio.addEventListener('change', function(){
                        delivery.text(parseFloat(radio.value).toLocaleString('vi-VN'));
                        delivery.attr('data-value', radio.value);
                        let temp_price = parseFloat($('#temp-price').text().replaceAll('.', ""))
                        let total = temp_price + parseFloat(radio.value)
                        $('#total-price').text(parseFloat(total).toLocaleString('vi-VN'))
                    });
                });
                // let radios = document.getElementsByName('infoship');
                // radios.forEach(function(radio) {
                //     radio.addEventListener('change', handleRadioChange);
                // });
            }
        })
    }
}

function updateToTalPrice() {

    let totalPrice = parseFloat($('#temp-price').attr('data-value'));
    totalPrice -= parseFloat($('#voucher-product').attr('data-value'));
    totalPrice -= parseFloat($('#voucher-ship').attr('data-value'));
    totalPrice += parseFloat($('#delivery-price').attr('data-value'));
    $('#total-price').attr('data-value', totalPrice)
    $('#total-price').text(_formatPrice(totalPrice))
    console.log('totalPrice ',totalPrice)

}