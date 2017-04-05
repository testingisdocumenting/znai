scenario """ # Department users list

To get access to current list of active users
pass department name as the last parameter
"""

http.post("/echo", [id: 2, numberOfUsers: 234]) {
    numberOfUsers.should == 234
}
