package client;

import coreClient.Album;
import coreClient.Cover;
import coreClient.Search;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import calls.RestCalls;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class AlbumsController {

//    INDEX

    @GetMapping("/albums")
    public String getAlbums(@ModelAttribute Search search, Model model) {
        List<Album> albums = RestCalls.GetAlbums();

        // Apply filter
        if (search.getText() != null) {
            String text = search.getText().toLowerCase(Locale.ROOT);

            albums = albums.stream()
                    .filter(album ->
                            album.getTitle().toLowerCase(Locale.ROOT).contains(text) ||
                                    album.getArtist().getFirstName().toLowerCase(Locale.ROOT).contains(text) ||
                                    album.getArtist().getLastName().toLowerCase(Locale.ROOT).contains(text) ||
                                    album.getDescription().toLowerCase(Locale.ROOT).contains(text)
                    )
                    .collect(Collectors.toList());
        }

        model.addAttribute("albums", albums);
        model.addAttribute("search", new Search());

        return "albums";
    }

//    SHOW

    @GetMapping("/albums/{isrc}")
    public String getAlbum(@PathVariable("isrc") String isrc, Model model) {
        Album album = RestCalls.GetAlbum(isrc);
        model.addAttribute("album", album);

        return "show";
    }

//    NEW

    @GetMapping("/albums/new")
    public String newAlbum(Model model) {
        model.addAttribute("album", new Album());

        return "new";
    }

    @PostMapping("/albums/new")
    public String newAlbumSubmit(@ModelAttribute Album album, Model model) {
        album.setCover(new Cover(null, null));
        RestCalls.CreateAlbum(album);

        return "redirect:/albums";
    }

//    EDIT

    @GetMapping("/albums/edit/{isrc}")
    public String editAlbum(@PathVariable("isrc") String isrc, Model model) {
        Album album = RestCalls.GetAlbum(isrc);
        model.addAttribute("album", album);

        return "edit";
    }

    @PostMapping("/albums/edit/{isrc}")
    public String editAlbumSubmit(@ModelAttribute Album album, Model model) {
        album.setCover(new Cover(null, null));
        RestCalls.EditAlbum(album);

        return "redirect:/albums";
    }

//    DELETE

    @GetMapping("/albums/delete/{isrc}")
    public String deleteAlbum(@PathVariable("isrc") String isrc, Model model) {
        RestCalls.DeleteAlbum(isrc);

        return "redirect:/albums";
    }
}