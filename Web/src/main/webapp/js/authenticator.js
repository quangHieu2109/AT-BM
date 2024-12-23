$('#togglePublicKey').click(function() {
    var passwordField = $('#showPublickey');
    var icon = $(this).find('i');

    if (passwordField.attr('type') === 'password') {
        passwordField.attr('type', 'text');
        icon.removeClass('bi-eye').addClass('bi-eye-slash');
    } else {
        passwordField.attr('type', 'password');
        icon.removeClass('bi-eye-slash').addClass('bi-eye');
    }
});

$('#togglePrivateKey').click(function() {
    var passwordField = $('#showPrivatekey');
    var icon = $(this).find('i');

    if (passwordField.attr('type') === 'password') {
        passwordField.attr('type', 'text');
        icon.removeClass('bi-eye').addClass('bi-eye-slash');
    } else {
        passwordField.attr('type', 'password');
        icon.removeClass('bi-eye-slash').addClass('bi-eye');
    }
});
$('#reportKey').on('click', function (){
    $.ajax({
        url: '/createAuthenticator',
        type: 'POST',
        data: {
            'type':'reportKey'
        },
        success: function (repsonse) {
            alert(repsonse)
            loadAuth()
        },
        error: function (response) {
            console.log(response)
            alert(response.message)
        }
    })
})
function loadAuth(){
    $('#table_body').empty()
    $.ajax({
        url: '/authenticator',
        type: 'POST',

        success: function (response) {
            // console.log(response)
            $('#table_body').append(response)
        },
        error: function (response) {
            console.log(response)
            alert(response.message)
        }
    })
}
$(document).ready(loadAuth())