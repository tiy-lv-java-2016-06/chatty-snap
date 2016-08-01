function getPhotos(photoData) {
    for (var i in photoData) {
        var photo = photoData[i];
        var elemDelayTime = photo.lifeDuration;
        var elem = $("<img>");
        elem.attr("src", "photos/" + photo.filename);
        elem.attr("height", 200);
        elem.attr("width", 200);

        $("#photos").append(elem);
        $(elem).fadeIn(300).delay(elemDelayTime).fadeOut(500);
    }
    $.post("/delete");
}

function getPublicPhotos(publicPhotoData) {
    for (var i in publicPhotoData) {
        var photo = publicPhotoData[i];
        var elemDelayTime = photo.lifeDuration;
        var elem = $("<img>");
        elem.attr("src", "photos/" + photo.filename);
        elem.attr("height", 200);
        elem.attr("width", 200);

        $("#publicPhotos").append(elem);
        $(elem).fadeIn(600);
    }
}

function getUser(userData){
    var name = userData.name;
    if(userData.length == 0){
        $("#login").show();
    }
    else{
        $("#logout").show();
        $('#salute').show();
        $('h1[id = salute]').html('Welcome '+name+'!');
        $("#upload").show();
        $(".title").show();
        $.get("/public-photos", getPublicPhotos);
        $.get("/photos", getPhotos);
    }
}

$.get("/user", getUser);
