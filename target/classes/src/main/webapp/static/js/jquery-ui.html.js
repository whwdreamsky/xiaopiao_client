/*
 * jQuery UI Autocomplete HTML Extension
 *
 * Copyright 2010, Scott González (http://scottgonzalez.com)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 *
 * http://github.com/scottgonzalez/jquery-ui-extensions
 */
(function( $ ) {

var proto = $.ui.autocomplete.prototype,
    initSource = proto._initSource;

function filter( array, term ) {
    var matcher = new RegExp( $.ui.autocomplete.escapeRegex(term), "i" );
    return $.grep( array, function(value) {
        return matcher.test( $( "<div>" ).html( value.label || value.value || value ).text() );
    });
}

$.extend( proto, {
    _initSource: function() {
        if ( this.options.html && $.isArray(this.options.source) ) {
            this.source = function( request, response ) {
                response( filter( this.options.source, request.term ) );
            };
        } else {
            initSource.call( this );
        }
    },

    _renderItem: function( ul, item) {
        if (item.value.indexOf('<flag>') >= 0) {
            var tem = item.value.split('<flag>');
            item.value = tem[1];
            item.label = tem[1];
            return $( "<li></li>" )
                .data( "item.autocomplete", item )
                .append( $( "<span style = 'color: red;'></span>" )[ this.options.html ? "html" : "text" ]( item.label ) )
                .appendTo( ul );
        }
        else {
            return $( "<li>" )
                .append( $( "<div>" ).text( item.label ) )
                .appendTo( ul );
        }
    }
});

})( jQuery );
