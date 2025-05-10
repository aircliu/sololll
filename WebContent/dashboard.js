$(document).ready(function() {
    // Menu navigation
    $("#showAddStar").click(function() {
        $(".dashboard-section").hide();
        $("#addStarSection").show();
    });
    
    $("#showAddMovie").click(function() {
        $(".dashboard-section").hide();
        $("#addMovieSection").show();
    });
    
    $("#showMetadata").click(function() {
        $(".dashboard-section").hide();
        $("#metadataSection").show();
        loadMetadata();
    });
    
    // Add Star Form Submission
    $("#addStarForm").submit(function(event) {
        event.preventDefault();
        let formData = $(this).serialize();
        
        $.ajax({
            dataType: "json",
            data: formData,
            method: "POST",
            url: "api/add-star",
            success: function(result) {
                let msgDiv = $("#star-message");
                if (result.success) {
                    msgDiv.removeClass("error").addClass("success");
                    msgDiv.html("Star added successfully with ID: " + result.starId);
                } else {
                    msgDiv.removeClass("success").addClass("error");
                    msgDiv.html("Error: " + result.message);
                }
            }
        });
    });
    
    // Add Movie Form Submission
    $("#addMovieForm").submit(function(event) {
        event.preventDefault();
        let formData = $(this).serialize();
        
        $.ajax({
            dataType: "json",
            data: formData,
            method: "POST",
            url: "api/add-movie",
            success: function(result) {
                let msgDiv = $("#movie-message");
                if (result.success) {
                    msgDiv.removeClass("error").addClass("success");
                    msgDiv.html("Movie added successfully: " + result.message);
                } else {
                    msgDiv.removeClass("success").addClass("error");
                    msgDiv.html("Error: " + result.message);
                }
            }
        });
    });
    
    // Load metadata function
    function loadMetadata() {
        $.ajax({
            dataType: "json",
            method: "GET",
            url: "api/metadata",
            success: function(result) {
                let metadataDiv = $("#metadata-content");
                metadataDiv.empty();
                
                if (result.tables && result.tables.length > 0) {
                    let html = "";
                    result.tables.forEach(function(table) {
                        html += "<div class='metadata-table'>";
                        html += "<h3>Table: " + table.name + "</h3>";
                        html += "<table>";
                        html += "<tr><th>Column</th><th>Type</th></tr>";
                        
                        table.columns.forEach(function(column) {
                            html += "<tr>";
                            html += "<td>" + column.name + "</td>";
                            html += "<td>" + column.type + "</td>";
                            html += "</tr>";
                        });
                        
                        html += "</table>";
                        html += "</div>";
                    });
                    metadataDiv.html(html);
                } else {
                    metadataDiv.html("<p>No metadata available.</p>");
                }
            }
        });
    }
});