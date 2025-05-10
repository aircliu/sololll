$(document).ready(function() {
    $("#showAddStar").click(function() {
        $("#addStarSection").show();
        $("#addMovieSection").hide();
        $("#metadataSection").hide();
    });
    
    $("#showAddMovie").click(function() {
        $("#addStarSection").hide();
        $("#addMovieSection").show();
        $("#metadataSection").hide();
    });
    
    $("#showMetadata").click(function() {
        $("#addStarSection").hide();
        $("#addMovieSection").hide();
        $("#metadataSection").show();
        loadMetadata();
    });
    
    $("#addStarForm").submit(function(event) {
        event.preventDefault();
        $.ajax({
            dataType: "json",
            data: $(this).serialize(),
            method: "POST",
            url: "api/add-star",
            success: function(result) {
                if (result.success) {
                    $("#star-message").html(
                        "<p style='color:green'>Star added successfully with ID: " + result.starId + "</p>"
                    );
                } else {
                    $("#star-message").html(
                        "<p style='color:red'>Error: " + result.message + "</p>"
                    );
                }
            }
        });
    });
    
    $("#addMovieForm").submit(function(event) {
        event.preventDefault();
        $.ajax({
            dataType: "json",
            data: $(this).serialize(),
            method: "POST",
            url: "api/add-movie",
            success: function(result) {
                if (result.success) {
                    $("#movie-message").html(
                        "<p style='color:green'>Movie added successfully: " + result.message + "</p>"
                    );
                } else {
                    $("#movie-message").html(
                        "<p style='color:red'>Error: " + result.message + "</p>"
                    );
                }
            }
        });
    });
    
    function loadMetadata() {
        $.ajax({
            dataType: "json",
            method: "GET",
            url: "api/metadata",
            success: function(result) {
                let html = "";
                if (result.tables && result.tables.length > 0) {
                    result.tables.forEach(function(table) {
                        html += "<h3>Table: " + table.name + "</h3>";
                        html += "<table border='1'>";
                        html += "<tr><th>Column</th><th>Type</th></tr>";
                        
                        table.columns.forEach(function(column) {
                            html += "<tr>";
                            html += "<td>" + column.name + "</td>";
                            html += "<td>" + column.type + "</td>";
                            html += "</tr>";
                        });
                        
                        html += "</table><br>";
                    });
                } else {
                    html = "<p>No metadata available.</p>";
                }
                $("#metadata-content").html(html);
            }
        });
    }
});