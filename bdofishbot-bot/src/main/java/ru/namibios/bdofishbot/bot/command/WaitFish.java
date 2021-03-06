package ru.namibios.bdofishbot.bot.command;

import ru.namibios.bdofishbot.bot.ImageParser;
import ru.namibios.bdofishbot.bot.Screen;
import ru.namibios.bdofishbot.bot.template.Chars;
import ru.namibios.bdofishbot.cli.Application;
import ru.namibios.bdofishbot.cli.config.Path;

import java.awt.*;
import java.io.IOException;

public class WaitFish implements Command{

    private String file;
    private Screen screen;

	private ImageParser imageParser;

	public WaitFish() {}

	public WaitFish(String file) {
        this.file = file;

    }

	@Override
	public String getKey() {

		try {

			return getKeyByRegion(Application.getInstance().SPACE_OFFSET_X(), Application.getInstance().SPACE_OFFSET_Y());

		} catch (AWTException | IOException e) {
			e.printStackTrace();
		}

		return ShortCommand.IGNORE.getKey();
	}

	private String getRegionKey(Rectangle region) throws AWTException, IOException {

        if (file != null) {
            screen = new Screen(file, region);
        } else {
            screen = new Screen(region);
        }

		imageParser = new ImageParser(screen, Chars.values());
		imageParser.parse(Screen.WHITE);

        if (Application.getInstance().DEBUG_SCREEN() || Application.getInstance().DEBUG_WAITFISH()) {
            screen.saveImage(Path.DEBUG_WAITFISH);
        }

//		MatrixUtils.printMatrix(imageParser.getImageMatrix(), "0");

		return imageParser.getNumber();
	}

	private String getKeyByRegion(int offsetX, int offsetY) throws AWTException, IOException {

		Rectangle region = Application.getInstance().SPACE();

		int startX = region.x;
		int startY = region.y;

		String key = "";

		for (int x = startX; x <= startX + offsetX; x++) {
			region.x = x;
			for (int y = startY; y <= startY + offsetY; y++) {
				region.y = y;
				key = getRegionKey(region);
				if (!key.isEmpty()) {
					return key;
				}
			}
		}

		return key;
	}

}