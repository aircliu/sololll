function handleLoginResult(resultData) {
    if (resultData["success"]) {
        window.location.href = "dashboard.html";
    } else {
        let msgSection = $("#message-section");
        msgSection.empty();
        msgSection.append(
            "<p style='color:red'>" + resultData["message"] + "</p>"
        );
    }
}

$("#login-form").submit(function (event) {
    event.preventDefault();
    let formData = $(this).serialize();
    
    $.ajax({
        dataType: "json",
        data: formData,
        method: "POST",
        url: "api/employee-login",
        success: handleLoginResult
    });
});