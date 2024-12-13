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
