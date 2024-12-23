

function getUnconfirmOrders(username){
    $.ajax({
        url: '/orderSwing?username='+username,
        type: 'GET',

        success: function (repsonse) {
            console.log(repsonse)
        },
        error: function (response) {
            console.log(response)
        }
    })
}
function getOrderDetail(){
    $.ajax({
        url: '/orderSwing?type=getOrderDetail&orderId=1734328121273',
        type: 'GET',

        success: function (repsonse) {
            console.log(repsonse)
        },
        error: function (response) {
            console.log(response)
        }
    })
}
// getUnconfirmOrders('user1')
function signOrders(orderSignatures){
    $.ajax({
        url: '/orderSwing',
        type: 'POST',
        data: {
            'orderSignatures':orderSignatures
        },
        success: function (repsonse) {
            console.log(repsonse)
        },
        error: function (response) {
            console.log(response)
        }
    })
}
signOrders('[{"signature":"fCtWhyMpL/rwOHu/fvBCRPOZC4Xnv1+QouP2ioU+IuewgF/n0ZfybkC5iUumVOdxMJ5Q3fuTfWZJ4eaf7U7L8qvgQR7CQ3ySAHSSf8x9e3Slr3bN5iMSqCqpYrrelLcMGbROYD9OM427+6DdUMfVCQ3DjKPxoapVdK15E/NFrcIll4SCTHM9CYlSw0jnLCVIbFMZyfKm58iaRlEaDf2hAtoZDuTI9OZ+iKh/YItslqNufx/+xVeueV4uRMi9zhp+4p9dl/f3Gz8ftmMtbhpxSYqccUvcWIlP1+GlDVGR2Lk79uckSLSrTuLhyXZnm1eVrqjQ8VZhr+fQc1QENvS8aQ==","id":1734238184620}]')

function login(){
    $.ajax({
        url: '/user',
        type: 'POST',
        data: {
            'username':'user1',
            'password':'202CB962AC59075B964B07152D234B70', // password sau khi dùng HashingUtils.hash(password)
        },
        success: function (repsonse) {
            console.log(repsonse)
        },
        error: function (response) {
            console.log(response)
        }
    })
}

function sendOTP(){
    $.ajax({
        url: '/authenticatorSwing',
        type: 'POST',
        data: {
            'username':'user1',
            'password':'202CB962AC59075B964B07152D234B70',// password sau khi dùng HashingUtils.hash(password)
            'type':'sendOTP'
        },
        success: function (repsonse) {
            console.log(repsonse)
        },
        error: function (response) {
            console.log(response)
        }
    })
}

function verifyOTP(){
    $.ajax({
        url: '/authenticatorSwing',
        type: 'POST',
        data: {
            'username':'user1',
            'password':'202CB962AC59075B964B07152D234B70',// password sau khi dùng HashingUtils.hash(password)
            'type':'verifyOTP',
            'otp':'123123'
        },
        success: function (repsonse) {
            console.log(repsonse)
        },
        error: function (response) {
            console.log(response)
        }
    })
}

function savePublicKey(){
    $.ajax({
        url: '/authenticatorSwing',
        type: 'POST',
        data: {
            'username':'user1',
            'password':'202CB962AC59075B964B07152D234B70',// password sau khi dùng HashingUtils.hash(password)
            'type':'savePublicKey',
            'publicKey':'123123'
        },
        success: function (repsonse) {
            console.log(repsonse)
        },
        error: function (response) {
            console.log(response)
        }
    })
}
function getAuthenticator(){
    $.ajax({
        url: '/authenticatorSwing',
        type: 'GET',
        data: {
            'username':'user1'

        },
        success: function (repsonse) {
            console.log(repsonse)
        },
        error: function (response) {
            console.log(response)
        }
    })
}
function reportKey(){
    $.ajax({
        url: '/authenticatorSwing',
        type: 'POST',
        data: {
            'username':'user1',
            'password':'202CB962AC59075B964B07152D234B70',// password sau khi dùng HashingUtils.hash(password)
            'type':'reportKey'
        },
        success: function (repsonse) {
            console.log(repsonse)
        },
        error: function (response) {
            console.log(response)
        }
    })
}

function checkEdited(){
    $.ajax({
        url: '/orderSignature?orderId=33',
        type: 'GET',

        success: function (repsonse) {
            console.log(repsonse)
        },
        error: function (response) {
            console.log(response)
        }
    })
}