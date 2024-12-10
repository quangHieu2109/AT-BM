function onSubmit() {
    e.preventDefault();
    grecaptcha.ready(function () {
        grecaptcha.execute('reCAPTCHA_site_key', {action: 'submit'}).then(function (token) {

        });
    });
}
