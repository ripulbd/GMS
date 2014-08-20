var mongo = require('mongodb');
var monk = require('monk');
var db = monk('localhost:27017/testdb');
var collectionName = "tempdb";

/*
 * GET home page.
 */

exports.index = function(req, res){	
	var collection = db.get(collectionName);
	collection.find({},{},function(e,docs){
        res.render('index', {
            "userlist" : docs,
            "title"	: 'Trying out!'
        });
    });
  //res.render('index', { title: 'Glasgow Memory Server (GMS): Home Page!' });
};