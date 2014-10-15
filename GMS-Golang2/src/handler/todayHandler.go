package handler

import (
	//"bufio"
	//"flag"
	"gopkg.in/mgo.v2"
	"gopkg.in/mgo.v2/bson"
	//"html/template"
	//"io/ioutil"
	"log"
	//"net"
	"net/http"
	//"text/template"
	//"os"
	//"regexp"
	//"strings"
	"encoding/json"
	"fmt"
	//"unicode/utf8"
	"time"
)

type Comment struct {
	UserName    string    `bson:"userName"`
	TimeStamp   string    `bson:"timeStamp"`
	CommentBody string    `bson:"commentBody"`
	UpVote      int       `bson:"upVote"`
	DownVote    int       `bson:"downVote"`
	Replies     []Comment `bson:"replies"`
}

type Image struct {
	Name    string `bson:"name"`
	Caption string `bson:"caption"`
}

type Page struct {
	Title       string    `bson:"title"`
	Description string    `bson:"description"`
	TimeStamp   string    `bson:"timeStamp"`
	Category    string    `bson:"category"`
	Url         string    `bson:"url"`
	Source      string    `bson:"source"`
	MainStory   string    `bson:"mainStory"`
	Images      []Image   `bson:"images"`
	Related     []string  `bson:"related"`
	Videos      []string  `bson:"videos"`
	Comments    []Comment `bson:"comments"`
	//Id			int
}

type TopPage struct {
	Headline string
	Pages    []Page
}

type IndexPage struct {
	Headline string
	BBCToday      []Page
	DRToday       []Page
	ScotToday     []Page
	ETToday       []Page
	
	BBC      []Page
	DR       []Page
	Scot     []Page
	ET       []Page
}

type TodayPage struct {	
	BBCToday      []Page
	DRToday       []Page
	ScotToday     []Page
	ETToday       []Page
}

func todayLatestHandler(w http.ResponseWriter, r *http.Request) {
	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()
	fmt.Printf("We are here.....")

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)

	currenttime := time.Now().Local()

	//fmt.Println("Current time : ", currenttime.Format("02/01/2006"))
	//fmt.Println("Previous Day : ", currenttime.AddDate(0,0,-1).Format("02/01/2006"))
	
	c := session.DB("gmsTry").C("gmsNews")
	
	scotResult := []Page{}	
	//err = c.Find(bson.M{"source": "http://www.scotsman.com"}).Sort("-$natural").Limit(5).All(&scotResult)
	err = c.Find(bson.M{"$and": []bson.M{{"source": "http://www.scotsman.com"}, {"timeStamp":bson.M{"$regex":currenttime.Format("02/01/2006"), "$options":"i"}}}}).Limit(5).All(&scotResult)	
	
	if err != nil {
		log.Fatal(err)
	}
		
	
	bbcResult := []Page{}		
	err = c.Find(bson.M{"$and": []bson.M{{"source": "http://www.bbc.co.uk"}, {"timeStamp":bson.M{"$regex":currenttime.Format("02/01/2006"), "$options":"i"}}}}).Limit(5).All(&bbcResult)
	
	if err != nil {
		log.Fatal(err)
	}	
	
	drResult := []Page{}		
	err = c.Find(bson.M{"$and": []bson.M{{"source": "http://www.dailyrecord.co.uk"}, {"timeStamp":bson.M{"$regex":currenttime.Format("02/01/2006"), "$options":"i"}}}}).Limit(5).All(&drResult)
	
	if err != nil {
		log.Fatal(err)
	}
	
	etResult := []Page{}		
	err = c.Find(bson.M{"$and": []bson.M{{"source": "Evening Times"}, {"timeStamp":bson.M{"$regex":currenttime.Format("02/01/2006"), "$options":"i"}}}}).Limit(5).All(&etResult)
	
	if err != nil {
		log.Fatal(err)
	}
	
	todayPage := TodayPage{bbcResult, drResult, scotResult, etResult}	
	todayPageArray := []TodayPage{todayPage}
		
	js, err := json.Marshal(todayPageArray)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
		
	w.Header().Set("Content-Type", "application/json")
  	w.Write(js)
}


func todayDiscussedHandler(w http.ResponseWriter, r *http.Request) {
	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)
	///////////Scot////////////////////	
	c := session.DB("gmsTry").C("gmsNews")

	o1 := bson.M{
		"$match": bson.M{"source": "http://www.scotsman.com"},
	}

	o2 := bson.M{
		"$unwind": "$comments",
	}

	o3 := bson.M{
		"$group": bson.M{
			"_id": "$url",
			"size": bson.M{
				"$sum": 1,
			},
		},
	}

	o4 := bson.M{
		"$sort": bson.M{
			"size": -1,
		},
	}

	o5 := bson.M{
		"$limit": 5,
	}
	
	scotSortResult := []Page{}
	

	operations := []bson.M{o1, o2, o3, o4, o5}

	pipe := c.Pipe(operations)

	// Run the queries and capture the results
	results := []bson.M{}
	err1 := pipe.All(&results)

	if err1 != nil {
		fmt.Printf("ERROR : %s\n", err1.Error())
		//return
	}
	if len(results) > 0 {
		for i := 0; i < 5; i++ {
			tmpPage := []Page{}
			url := results[i]["_id"]
			err = c.Find(bson.M{"url": url}).All(&tmpPage)		
			scotSortResult = append(scotSortResult, tmpPage[0])
		}
	}
	fmt.Printf("Scotsman Today Discussed : %d\n", len(results))
	///////////Scot////////////////////
	
	///////////BBC////////////////////
	o1 = bson.M{
		"$match": bson.M{"source": "http://www.bbc.co.uk"},
	}	
	
	bbcSortResult := []Page{}

	operations = []bson.M{o1, o2, o3, o4, o5}

	pipe = c.Pipe(operations)

	// Run the queries and capture the results
	results = []bson.M{}
	err1 = pipe.All(&results)

	if err1 != nil {
		fmt.Printf("ERROR : %s\n", err1.Error())
		//return
	}
	if len(results) > 0 {
		for i := 0; i < 5; i++ {
			tmpPage := []Page{}
			url := results[i]["_id"]
			err = c.Find(bson.M{"url": url}).All(&tmpPage)
			bbcSortResult = append(bbcSortResult, tmpPage[0])
		}
	}
	fmt.Printf("BBC Today Discussed : %d\n", len(results))
	///////////BBC////////////////////
	
	///////////DR////////////////////
	o1 = bson.M{
		"$match": bson.M{"source": "http://www.dailyrecord.co.uk"},
	}	
	
	drSortResult := []Page{}

	operations = []bson.M{o1, o2, o3, o4, o5}

	pipe = c.Pipe(operations)

	// Run the queries and capture the results
	results = []bson.M{}
	err1 = pipe.All(&results)

	if err1 != nil {
		fmt.Printf("ERROR : %s\n", err1.Error())
		//return
	}

	if len(results) > 0 {
		for i := 0; i < 5; i++ {
			tmpPage := []Page{}
			url := results[i]["_id"]
			err = c.Find(bson.M{"url": url}).All(&tmpPage)		
			drSortResult = append(drSortResult, tmpPage[0])
		}
	}
	fmt.Printf("DR Today Discussed : %d\n", len(results))
	///////////DR////////////////////
	
	///////////ET////////////////////
	o1 = bson.M{
		"$match": bson.M{"source": "Evening Times"},
	}	
	
	etSortResult := []Page{}

	operations = []bson.M{o1, o2, o3, o4, o5}

	pipe = c.Pipe(operations)

	// Run the queries and capture the results
	results = []bson.M{}
	err1 = pipe.All(&results)

	if err1 != nil {
		fmt.Printf("ERROR : %s\n", err1.Error())
		//return
	}

	if len(results) > 0 {
		for i := 0; i < 5; i++ {
			tmpPage := []Page{}
			url := results[i]["_id"]
			err = c.Find(bson.M{"url": url}).All(&tmpPage)
		
			etSortResult = append(etSortResult, tmpPage[0])
		}
	}
	fmt.Printf("ET Today Discussed : %d\n", len(results))
	///////////ET////////////////////
	
	todayPage := TodayPage{bbcSortResult, drSortResult, scotSortResult, etSortResult}
	todayPageArray := []TodayPage{todayPage}
	
	js, err := json.Marshal(todayPageArray)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	
	w.Header().Set("Content-Type", "application/json")
  	w.Write(js)
}