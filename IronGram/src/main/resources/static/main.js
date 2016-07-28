function getPhotos(photoData){
    for(var i in photoData){
        var photo = photoData[i];
        var elem = $("<img>");
        elem.attr("src", "photos/" + photo.filename);
        elem.attr("height", 200);
        elem.attr("width", 200);

        $("#photos").append(elem);
    }
}

function getUser(userData){
    if(userData.length == 0){
        $("#login").show();
    }
    else{
        $("#logout").show();
        $("#upload").show();
        $.get("/photos", getPhotos);
    }
}

$.get("/user", getUser);
