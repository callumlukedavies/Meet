package com.example.meet.model

data class Event(val eventName: String, val eventDate: String,
                 val eventTime: String, val eventPlaceName: String,
                 val eventPlaceId: String, val eventId: String,
                 val organiserId: String, val eventDescription: String,
                 val invitedList: MutableList<String>,
                 val attendingList: MutableList<String>,
                 var eventMessages: HashMap<String, HashMap<String, String>>) {


}