package com.smartcl.androidwearsmartbeacon;

import android.content.Intent;
import android.net.Uri;

import com.android.volley.VolleyError;
import com.google.android.gms.wearable.MessageEvent;
import com.smartcl.communicationlibrary.BaseListenerService;

import org.json.simple.JSONObject;

/**
 * Listener which reacts to the messages sent by the wearable device.
 * These messages can mean we detected a smartbeacon, or we got an answer from a question.
 * <p/>
 * When a smartbeacon is found, a question is sent to the wearable device.
 * The answer is saved within the database on the server side..
 */
public class ListenerService extends BaseListenerService {

    public static final String BEACON_ENTERED_PATH = "/beacon/entered/";
    public static final String QUESTION_ANSWER_PATH = "/question/answer/";
    public static final String WEBSITE_OPEN_PATH = "/website/open/lcl/";
    public static final String QUESTION_QUESTION_PATH = "/question/question";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String path = messageEvent.getPath();
        switch (path) {
            case BEACON_ENTERED_PATH:
                beaconHasEntered(messageEvent);
                break;
            case QUESTION_ANSWER_PATH:
                getAnswer(messageEvent);
                break;
            case WEBSITE_OPEN_PATH:
                openWebsite(messageEvent);
                break;
            default:
                showToast("Unknown message:" + path);
                break;
        }
    }

    private void getAnswer(MessageEvent messageEvent) {
        JSONObject json = extractJsonFromMessage(messageEvent);
        final String answer = (String) json.get("answer");
        final String question = (String) json.get("question");
        final String name = (String) json.get("name");

        final String message = messageEvent.getPath() + "Answer[" + answer + "]";
        showToast(message);

        NetworkOperation network = new NetworkOperation(this);
        NetworkAnswerGetAnswer networkAnswerGetAnswer = new NetworkAnswerGetAnswer(network);
        network.operationGet(networkAnswerGetAnswer.getAnswerUrl(question, name, answer),
                             networkAnswerGetAnswer);
    }

    private void beaconHasEntered(MessageEvent messageEvent) {
        JSONObject json = extractJsonFromMessage(messageEvent);
        final long major = (long) json.get("major");
        final long minor = (long) json.get("minor");

        final String message = messageEvent.getPath() + "Major[" + major + "]Minor[" + minor + "]";
        showToast(message);

        NetworkOperation network = new NetworkOperation(this);
        NetworkAnswerStudentInfo networkAnswerStudentInfo = new NetworkAnswerStudentInfo(network);
        network.operationGet(networkAnswerStudentInfo.getSignInUrl("Olivier"),
                             networkAnswerStudentInfo);
    }

    private void openWebsite(MessageEvent messageEvent) {
        JSONObject json = extractJsonFromMessage(messageEvent);
        final String path = (String) json.get("path");

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(browserIntent);
    }

    class NetworkAnswerStudentInfo extends NetworkAnswer {

        public NetworkAnswerStudentInfo(NetworkOperation network) {
            super(network);
        }

        public String getSignInUrl(String name) {
            return R.string.server_url + "user/signin?name=" + name;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("FAIL");
        }

        @Override
        public void onResponse(Object response) {
            showToast("It works: get status");
            super.onResponse(response);
        }

        @Override
        public void run(Object response) {

//            JSONParser parser = new JSONParser();
//                Object obj = parser.parse((String) response);

//                JSONObject json = (JSONObject) obj;
            //TODO: get status
//            String status = (String) json.get("status");
//                showToast("Status=" + status);
            final String status = "student";

            NetworkAnswerGetQuestion networkAnswerGetQuestion = new NetworkAnswerGetQuestion(
                    _network);
            _network.operationGet(networkAnswerGetQuestion.getQuestionUrl(status),
                                  networkAnswerGetQuestion);
        }
    }

    class NetworkAnswerGetQuestion extends NetworkAnswer {

        public NetworkAnswerGetQuestion(NetworkOperation network) {
            super(network);
        }

        public String getQuestionUrl(String status) {
            return R.string.server_url + "question/ask?status=" + status;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("FAIL question");
        }

        @Override
        public void onResponse(Object response) {
            showToast("It works: get question");
            final String question = "Question gotten ! This is a long question to test if it works well when it's long";

            JSONObject json = new JSONObject();
            json.put("question", question);
            _messageSender.sendMessage(QUESTION_QUESTION_PATH, json);
        }
    }


    class NetworkAnswerGetAnswer extends NetworkAnswer {

        public NetworkAnswerGetAnswer(NetworkOperation network) {
            super(network);
        }

        public String getAnswerUrl(String title, String name, String answer) {
            return R.string.server_url + "question/answer?title=" + title + "&name=" + name +
                    "&answer=" +
                    answer;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("FAIL question answer");
        }

        @Override
        public void onResponse(Object response) {
            showToast("It works: answer");
        }
    }

}
