package ru.dictionary.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import ru.dictionary.entity.Word;
import ru.dictionary.testData.WordTestData;
import ru.dictionary.web.json.JsonUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static ru.dictionary.testData.WordTestData.*;
import static ru.dictionary.testData.WordTestData.WORD_MATCHER;

class WordRestControllerTest extends AbstractControllerTest{
    private static final String REST_URL = WordRestController.REST_URL + "/";

    @Test
    void save() throws Exception {
        MvcResult mvcResult = perform(post(REST_URL)
                .content(JsonUtil.writeValue(NEW_WORD))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        Word actual = JsonUtil.readValue(mvcResult.getResponse().getContentAsString(), Word.class);
        Word expected = new Word(actual.getId(), NEW_WORD.getWord(), NEW_WORD.getTranslation());

        WORD_MATCHER.assertMatch(actual, expected);
    }

    @Test
    void update() throws Exception {
        perform(put(REST_URL)
                .content(JsonUtil.writeValue(UPDATED_APPLE))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void deleteWord() throws Exception {
        perform(delete(REST_URL + APPLE_ID))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void deleteWords() throws Exception {
        perform(delete(REST_URL)
                .param("ids", WORD_IDS))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void getWord() throws Exception {
        perform(get(REST_URL + WordTestData.APPLE_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(WORD_MATCHER.contentJson(APPLE));
    }

    @Test
    void getWords() throws Exception {
        perform(get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(result -> WORD_MATCHER.contentJson(ALL_WORDS_FOR_USER));
    }
}
