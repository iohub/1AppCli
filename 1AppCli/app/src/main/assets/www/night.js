
function set_night_mode(mode) {
    if ( mode ) {
        document.body.className += 'night ';
    } else {
        document.body.className = '';
    }
    document.body.style.visibility = 'visible';
}