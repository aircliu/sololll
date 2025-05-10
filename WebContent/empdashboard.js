$(document).ready(function() {
    $("#login-form").submit(function(event) {
        event.preventDefault();
        let formData = $(this).serialize();
        
        $.ajax({
            dataType: "json",
            data: formData,
            method: "POST",
            url: "api/employee-login",
            success: function(resultData) {
                if (resultData.success) {
                    window.location.href = "employeedashboard.html";
                } else {
                    $("#message-section").html(
                        "<p style='color:red'>" + resultData.message + "</p>"
                    );
                }
            }
        });
    });
});