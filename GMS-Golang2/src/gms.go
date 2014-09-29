package main

import (
	//"bufio"
	"flag"
	"gopkg.in/mgo.v2"
	"gopkg.in/mgo.v2/bson"
	//"html/template"
	"io/ioutil"
	"log"
	"net"
	"net/http"
	"text/template"
	//"os"
	"regexp"
	//"strings"
	"encoding/json"
	"fmt"
	"unicode/utf8"
)

var (
	addr = flag.Bool("addr", false, "find open address and print to final-port.txt")
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
	BBC      []Page
	DR       []Page
	Scot     []Page
	ET       []Page
}

var templates = template.Must(template.ParseFiles("index.html", "news.html", "detailNews.html"))

func renderTemplate(w http.ResponseWriter, tmpl string, p *TopPage) {
	// Execute the template for each recipient.
	err := templates.ExecuteTemplate(w, tmpl+".html", p)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
	/*for _, r := range *p {
		err := templates.ExecuteTemplate(w, tmpl+".html", r)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
		}
	}*/

}

func renderIndexTemplate(w http.ResponseWriter, tmpl string, p *IndexPage) {
	// Execute the template for each recipient.
	err := templates.ExecuteTemplate(w, tmpl+".html", p)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
	/*for _, r := range *p {
		err := templates.ExecuteTemplate(w, tmpl+".html", r)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
		}
	}*/

}

func renderDetailNews(w http.ResponseWriter, tmpl string, p *Page) {
	// Execute the template for each recipient.
	err := templates.ExecuteTemplate(w, tmpl+".html", p)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
}

func indexHandler(w http.ResponseWriter, r *http.Request) {
	//p := &Page{Title: title}

	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)

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

	bbcResult := []Page{}
	//err = c.Find(bson.M{}).All(&result)
	err = c.Find(bson.M{"source": "http://www.bbc.co.uk"}).Sort("-$natural").Limit(5).All(&bbcResult)

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

	// Capture the average wind speed
	//idURL := results[0]["_id"];
	//size := results[0]["size"];

	//fmt.Printf("URL : %s, Size: %d\n", idURL, size)

	for i := 0; i < 5; i++ {
		tmpPage := []Page{}
		url := results[i]["_id"]
		err = c.Find(bson.M{"url": url}).All(&tmpPage)
		if utf8.RuneCountInString(tmpPage[0].Description) > 150 {
			tmpPage[0].Description = tmpPage[0].Description[:140] + "..."
		}

		scotSortResult = append(scotSortResult, tmpPage[0])
	}

	drResult := []Page{}	
	err = c.Find(bson.M{"source": "http://www.dailyrecord.co.uk"}).Sort("-$natural").Limit(5).All(&drResult)

	etResult := []Page{}
	//err = c.Find(bson.M{}).All(&result)
	err = c.Find(bson.M{"source": "Evening Times"}).Sort("-$natural").Limit(5).All(&etResult)

	scotResult := []Page{}
	//err = c.Find(bson.M{}).All(&result)
	err = c.Find(bson.M{"source": "http://www.scotsman.com"}).Sort("-$natural").Limit(5).All(&scotResult)

	if utf8.RuneCountInString(bbcResult[0].Description) > 150 {
		bbcResult[0].Description = bbcResult[0].Description[:140] + "..."
	}
	
	if utf8.RuneCountInString(etResult[0].Description) > 150 {
		etResult[0].Description = etResult[0].Description[:140] + "..."
	}
	if utf8.RuneCountInString(drResult[0].Description) > 150 {
		drResult[0].Description = drResult[0].Description[:140] + "..."
	}
	if utf8.RuneCountInString(scotResult[0].Description) > 150 {
		scotResult[0].Description = scotResult[0].Description[:140] + "..."
	}

	if err != nil {
		log.Fatal(err)
	}

	indexPage := IndexPage{"Top 5 contents from each source are shown below:", bbcResult, drResult, scotResult, etResult}

	renderIndexTemplate(w, "index", &indexPage)
}

func etHandler(w http.ResponseWriter, r *http.Request) {
	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)

	c := session.DB("gmsTry").C("gmsNews")

	result := []Page{}
	err = c.Find(bson.M{"source": "Evening Times"}).All(&result)

	if err != nil {
		log.Fatal(err)
	}
	topPage := TopPage{"News contents extracted from Evening Times are shown below:", result}

	renderTemplate(w, "news", &topPage)
}

func detailNewsHandler(w http.ResponseWriter, r *http.Request) {

	title := r.FormValue("query")
	fmt.Printf("Query: %s\n", title)
	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)

	c := session.DB("gmsTry").C("gmsNews")

	var result Page
	err = c.Find(bson.M{"title": title}).One(&result)

	if err != nil {
		log.Fatal(err)
	}

	renderDetailNews(w, "detailNews", &result)
}

func scotmanHandler(w http.ResponseWriter, r *http.Request) {
	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)

	c := session.DB("gmsTry").C("gmsNews")

	result := []Page{}
	err = c.Find(bson.M{"source": "http://www.scotsman.com"}).All(&result)

	if err != nil {
		log.Fatal(err)
	}
	topPage := TopPage{"News contents extracted from Scotsman are shown below:", result}

	renderTemplate(w, "news", &topPage)
}

func drHandler(w http.ResponseWriter, r *http.Request) {
	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)

	c := session.DB("gmsTry").C("gmsNews")

	result := []Page{}
	err = c.Find(bson.M{"source": "http://www.dailyrecord.co.uk"}).All(&result)

	if err != nil {
		log.Fatal(err)
	}
	topPage := TopPage{"News contents extracted from DailyRecord are shown below:", result}

	renderTemplate(w, "news", &topPage)
}

func bbcHandler(w http.ResponseWriter, r *http.Request) {
	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)

	c := session.DB("gmsTry").C("gmsNews")

	result := []Page{}
	err = c.Find(bson.M{"source": "http://www.bbc.co.uk"}).All(&result)

	if err != nil {
		log.Fatal(err)
	}
	topPage := TopPage{"News contents extracted from BBC are shown below:", result}

	renderTemplate(w, "news", &topPage)
}

func bbcLatestHandler(w http.ResponseWriter, r *http.Request) {
	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)

	c := session.DB("gmsTry").C("gmsNews")

	result := []Page{}
	err = c.Find(bson.M{"source": "http://www.bbc.co.uk"}).All(&result)

	if err != nil {
		log.Fatal(err)
	}
	topPage := TopPage{"News contents extracted from BBC are shown below:", result}

	renderTemplate(w, "news", &topPage)
}

func scotLatestHandler(w http.ResponseWriter, r *http.Request) {
	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()
	fmt.Printf("We are here.....")

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)

	c := session.DB("gmsTry").C("gmsNews")
	
	scotResult := []Page{}	
	err = c.Find(bson.M{"source": "http://www.scotsman.com"}).Sort("-$natural").Limit(5).All(&scotResult)	

	if err != nil {
		log.Fatal(err)
	}
	
	if utf8.RuneCountInString(scotResult[0].Description) > 150 {
			scotResult[0].Description = scotResult[0].Description[:140] + "..."
		}

	js, err := json.Marshal(scotResult)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
		
	w.Header().Set("Content-Type", "application/json")
  	w.Write(js)
}


func scotDiscussedHandler(w http.ResponseWriter, r *http.Request) {
	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)

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

	for i := 0; i < 5; i++ {
		tmpPage := []Page{}
		url := results[i]["_id"]
		err = c.Find(bson.M{"url": url}).All(&tmpPage)
		
		if utf8.RuneCountInString(tmpPage[0].Description) > 150 {
			tmpPage[0].Description = tmpPage[0].Description[:140] + "..."
		}
		scotSortResult = append(scotSortResult, tmpPage[0])
	}
	
	js, err := json.Marshal(scotSortResult)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	
	w.Header().Set("Content-Type", "application/json")
  	w.Write(js)
}

func etLatestHandler(w http.ResponseWriter, r *http.Request) {
	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()
	fmt.Printf("We are here.....")

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)

	c := session.DB("gmsTry").C("gmsNews")
	
	etResult := []Page{}	
	err = c.Find(bson.M{"source": "Evening Times"}).Sort("-$natural").Limit(5).All(&etResult)	

	if err != nil {
		log.Fatal(err)
	}
	
	if utf8.RuneCountInString(etResult[0].Description) > 150 {
			etResult[0].Description = etResult[0].Description[:140] + "..."
		}

	js, err := json.Marshal(etResult)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
		
	w.Header().Set("Content-Type", "application/json")
  	w.Write(js)
}


func etDiscussedHandler(w http.ResponseWriter, r *http.Request) {
	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)

	c := session.DB("gmsTry").C("gmsNews")

	o1 := bson.M{
		"$match": bson.M{"source": "Evening Times"},
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
	
	etSortResult := []Page{}

	operations := []bson.M{o1, o2, o3, o4, o5}

	pipe := c.Pipe(operations)

	// Run the queries and capture the results
	results := []bson.M{}
	err1 := pipe.All(&results)

	if err1 != nil {
		fmt.Printf("ERROR : %s\n", err1.Error())
		//return
	}

	for i := 0; i < 5; i++ {
		tmpPage := []Page{}
		url := results[i]["_id"]
		err = c.Find(bson.M{"url": url}).All(&tmpPage)
		
		if utf8.RuneCountInString(tmpPage[0].Description) > 150 {
			tmpPage[0].Description = tmpPage[0].Description[:140] + "..."
		}
		etSortResult = append(etSortResult, tmpPage[0])
	}
	
	js, err := json.Marshal(etSortResult)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	
	w.Header().Set("Content-Type", "application/json")
  	w.Write(js)
}

//source: ''

var validPath = regexp.MustCompile("^/(index|save|view)$")

func makeHandler(fn func(http.ResponseWriter, *http.Request)) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		/*m := validPath.FindStringSubmatch(r.URL.Path)
		if m == nil {
			http.NotFound(w, r)
			return
		}*/

		//title := result.Title
		//fmt.Println("Phone:", result.mainStory)

		fn(w, r)
	}
}
func main() {
	flag.Parse()
	http.HandleFunc("/", makeHandler(indexHandler))
	http.HandleFunc("/et", makeHandler(etHandler))
	http.HandleFunc("/scotsman", makeHandler(scotmanHandler))
	http.HandleFunc("/bbc", makeHandler(bbcHandler))
	http.HandleFunc("/bbcLatest", makeHandler(bbcLatestHandler))
	http.HandleFunc("/scotLatest", makeHandler(scotLatestHandler))
	http.HandleFunc("/scotDiscussed", makeHandler(scotDiscussedHandler))
	http.HandleFunc("/etLatest", makeHandler(etLatestHandler))
	http.HandleFunc("/etDiscussed", makeHandler(etDiscussedHandler))
	http.HandleFunc("/dr", makeHandler(drHandler))
	http.HandleFunc("/detailNews", makeHandler(detailNewsHandler))
	http.Handle("/resources/", http.StripPrefix("/resources/", http.FileServer(http.Dir("resources"))))
	http.Handle("/resources/images/", http.StripPrefix("/resources/images/", http.FileServer(http.Dir("/home/ripul/resources/images/"))))

	if *addr {
		l, err := net.Listen("tcp", "127.0.0.1:0")
		if err != nil {
			log.Fatal(err)
		}
		err = ioutil.WriteFile("final-port.txt", []byte(l.Addr().String()), 0644)
		if err != nil {
			log.Fatal(err)
		}
		s := &http.Server{}
		s.Serve(l)
		return
	}

	http.ListenAndServe(":8090", nil)
}
