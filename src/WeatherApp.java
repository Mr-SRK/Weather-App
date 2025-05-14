import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;

import java.util.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class WeatherApp extends JPanel {
    JSONObject weatherData;
    JFrame f;
    JTextField city;
    JButton search;
    JLabel weatherPic;
    ImageIcon weather;
    JLabel temp;
    JLabel weatherDes;
    JLabel windSpeedPic;
    JLabel uvIndexPic;
    JLabel precipitationPic;
    JLabel humidityPic;
    JLabel windSpeedTitle;
    JLabel uvIndexTitle;
    JLabel precipitationTitle;
    JLabel humidityTitle;
    JLabel windSpeed;
    JLabel uvIndex;
    JLabel precipitation;
    JLabel humidity;
    JLabel uvDes;
    JLabel fullDate;

    public WeatherApp() {
        setLayout(null);
        setPreferredSize(new Dimension(400, 500));
        setBackground(new Color(200, 240, 255));

        System.out.println("Working directory: " + System.getProperty("user.dir"));

        city = new JTextField();
        city.setBounds(50, 10, 250, 30);
        city.setFont(new Font("Open Sans", Font.BOLD, 20));

        ImageIcon searchImage = new ImageIcon(getClass().getResource("/assets/magglass.png"));
        Image scaledImage = searchImage.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        search = new JButton(scaledIcon);
        search.setBounds(310, 10, 40, 30);
        search.addActionListener(new SearchListener());

        weatherPic = new JLabel();
        weatherPic.setBounds(120, 5, 250, 250);

        temp = new JLabel("---");
        temp.setBounds(170, 205, 100, 100);
        temp.setFont(new Font("Open Sans", Font.BOLD, 35));

        weatherDes = new JLabel("----");
        weatherDes.setBounds(175, 255, 100, 50);
        weatherDes.setFont(new Font("Open Sans", Font.PLAIN, 20));

        fullDate = new JLabel("---");
        fullDate.setBounds(175, 265, 150, 100);
        fullDate.setFont(new Font("Open Sans", Font.BOLD, 17));

        windSpeedPic = new JLabel();
        windSpeedPic.setIcon(photoResize("icons8-wind-symbol-96.png"));
        windSpeedPic.setBounds(10, 320, 80, 80);

        precipitationPic = new JLabel();
        precipitationPic.setIcon(photoResize("precipitation.png"));
        precipitationPic.setBounds(10, 410, 80, 80);

        windSpeed = new JLabel("--- Km/hr");
        windSpeed.setBounds(95, 330, 100, 30);
        windSpeed.setFont(new Font("Open Sans", Font.PLAIN, 20));

        windSpeedTitle = new JLabel("Wind Speed");
        windSpeedTitle.setBounds(95,355, 100, 30);
        windSpeedTitle.setFont(new Font("Open Sans", Font.BOLD, 17));

        precipitation = new JLabel("--- mm");
        precipitation.setBounds(95, 420, 100, 30);
        precipitation.setFont(new Font("Open Sans", Font.PLAIN, 20));

        precipitationTitle = new JLabel("Precipitation");
        precipitationTitle.setBounds(95, 445, 130, 30);
        precipitationTitle.setFont(new Font("Open Sans", Font.BOLD, 17));

        humidityPic = new JLabel();
        humidityPic.setIcon(photoResize("humidity.png"));
        humidityPic.setBounds(215, 320, 80, 80);

        uvIndexPic = new JLabel();
        uvIndexPic.setIcon(photoResize("uv.png"));
        uvIndexPic.setBounds(220, 410, 80, 80);

        humidity = new JLabel("--- %");
        humidity.setBounds(305, 330, 100, 30);
        humidity.setFont(new Font("Open Sans", Font.PLAIN, 20));

        humidityTitle = new JLabel("Humidity");
        humidityTitle.setBounds(300, 354, 100, 30);
        humidityTitle.setFont(new Font("Open Sans", Font.BOLD, 17));

        uvIndex = new JLabel("--");
        uvIndex.setBounds(315, 405, 100, 30);
        uvIndex.setFont(new Font("Open Sans", Font.BOLD, 20));

        uvDes = new JLabel("----");
        uvDes.setBounds(305, 425, 100, 30);
        uvDes.setFont(new Font("Open Sans", Font.PLAIN, 17));

        uvIndexTitle = new JLabel("UV Index");
        uvIndexTitle.setBounds(305, 445, 100, 30);
        uvIndexTitle.setFont(new Font("Open Sans", Font.BOLD, 17));

        add(city);
        add(search);
        add(temp);
        add(weatherDes);
        add(weatherPic);
        add(windSpeedPic);
        add(precipitationPic);
        add(windSpeedTitle);
        add(windSpeed);
        add(precipitation);
        add(precipitationTitle);
        add(humidityPic);
        add(humidity);
        add(humidityTitle);
        add(uvIndexPic);
        add(uvIndex);
        add(uvDes);
        add(uvIndexTitle);
        add(fullDate);

        f = new JFrame();
        f.add(this);
        f.setSize(400, 500);
        f.pack();
        f.setLocationRelativeTo(null);
        Image img = new ImageIcon(this.getClass().getResource("/assets/icon.png")).getImage();
        f.setIconImage(img);
        f.setTitle("Weather App");
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setResizable(false);

    }

    public static String weatherVariable(long weatherCode) {
        if (weatherCode == 0) {
            return "Clear";
        } else if (weatherCode >= 1 && weatherCode <= 2) {
            return "Partly Cloudy";
        } else if (weatherCode == 3) {
            return "Cloudy";
        } else if (weatherCode == 45 || weatherCode == 48) {
            return "Fog";
        } else if (weatherCode >= 51 || weatherCode <= 57) {
            return "Drizzle";
        } else if ((weatherCode >= 61 && weatherCode <= 65) || (weatherCode >= 80 && weatherCode <= 82)) {
            return "Rain";
        } else if (weatherCode >= 71 && weatherCode <= 77) {
            return "Snow";
        } else if (weatherCode == 85 || weatherCode == 86) {
            return "Snow Showers";
        } else if (weatherCode >= 95 && weatherCode <= 99) {
            return "Thunder Storm";
        }
        return "";
    }

    public static ImageIcon photoResize(String fName) {
        try {
            URL url = WeatherApp.class.getResource("/assets/" + fName);

            if (url == null) {
                throw new RuntimeException("Image not found: " + fName);
            }

            ImageIcon original = new ImageIcon(url);

            if (original.getImageLoadStatus() != MediaTracker.COMPLETE) {
                throw new RuntimeException("Image failed to load: " + fName);
            }

            Image scaled = original.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);

        } catch (Exception e) {
        e.printStackTrace();
        return null;
        }
    }
    
    public static ImageIcon setWeatherPhoto(String fName) {
        return new ImageIcon(new ImageIcon(WeatherApp.class.getResource("/assets/" + fName))
                .getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));

    }
    
    public static void labelAlignment(JLabel label, int y, int panelWidth) {
        Font font = label.getFont();
        FontMetrics metrics = label.getFontMetrics(font);

        int textWidth = metrics.stringWidth(label.getText());

        int x = (panelWidth - textWidth) / 2;
        int height = metrics.getHeight();

        label.setBounds(x, y, textWidth, height);
    }

    public static String uvIndexRange(double uv) {
        int uvIndex = (int) uv;
        if (uvIndex >= 0 && uvIndex <= 2) {
            return "Low";
        } else if (uvIndex >= 3 && uvIndex <= 5) {
            return "Moderate";
        } else if (uvIndex >= 6 && uvIndex <= 7) {
            return "High";
        } else if (uvIndex >= 8 && uvIndex <= 10) {
            return "Very High";
        } else if (uvIndex >= 11) {
            return "Extreme";
        }
        return "";

    }

    public static Date convertToDate(String dateTimeString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            return formatter.parse(dateTimeString);
        } catch (ParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
            return null;
        }
    }
    
    private class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (city.getText() != "") {
                String text = city.getText();
                weatherData = WeatherAPIData.getWeatherData(text);

                JSONObject currentWeather = (JSONObject) weatherData.get("current");
                JSONObject hourlyWeather = (JSONObject) weatherData.get("hourly");

                int panelWidth = WeatherApp.this.getWidth();

                double temperature = (double) currentWeather.get("temperature_2m");

                temp.setText("" + (int) Math.round(temperature) + "\u00B0" + "C");
                labelAlignment(temp, 205, panelWidth);

                String time = (String) currentWeather.get("time");
                Date date = convertToDate(time);

                fullDate.setText("" + date);

                long relativeHumidity = (long) currentWeather.get("relative_humidity_2m");
                humidity.setText("" + (int) relativeHumidity + "%");
                labelAlignment(fullDate, 280, panelWidth);

                
        
                double wind = (double) currentWeather.get("wind_speed_10m");
                windSpeed.setText("" + (int) (Math.round(wind)) + " Km/hr");
                

        
                JSONArray uvArray = (JSONArray) hourlyWeather.get("uv_index");
                double uv = ((Number) uvArray.get(0)).doubleValue();

                String uvIndicator = uvIndexRange(uv);
                uvDes.setText(uvIndicator);

                if (uvIndicator == "Low") {
                    uvIndex.setForeground(Color.GREEN);
                } else if (uvIndicator == "Moderate") {
                    uvIndex.setForeground(Color.YELLOW);
                } else if (uvIndicator == "High") {
                    uvIndex.setForeground(Color.ORANGE);
                } else if (uvIndicator == "Very High") {
                    uvIndex.setForeground(Color.RED);
                } else if (uvIndicator == "Extreme") {
                    uvIndex.setForeground(Color.MAGENTA);
                }
                uvIndex.setText("" + (int) (Math.round(uv)));
                

                double precip = (double) currentWeather.get("precipitation");
                precipitation.setText("" + precip + " mm");


                long isDay = (long) currentWeather.get("is_day");

                String weatherCode = weatherVariable((long) currentWeather.get("weather_code"));

                weatherDes.setText(weatherCode);
                labelAlignment(weatherDes, 250, panelWidth);

                if (weatherCode == "Clear" && isDay == 1) {
                    weatherPic.setIcon(setWeatherPhoto("yellow-sun-16526.png"));
                } else if (weatherCode == "Partly Cloudy" && isDay == 1) {
                    weatherPic.setIcon(setWeatherPhoto("yellow-sun-and-blue-cloud-16528.png"));
                } else if (weatherCode == "Cloudy" && isDay == 1) {
                    weatherPic.setIcon(setWeatherPhoto("blue-cloud-and-weather-16527.png"));
                } else if (weatherCode == "Fog" && isDay == 1) {
                    weatherPic.setIcon(setWeatherPhoto("foggy-cloud-forecast-24549.png"));
                } else if (weatherCode == "Drizzle" && isDay == 1) {
                    weatherPic.setIcon(setWeatherPhoto("rainy-and-cloudy-day-16532.png"));
                } else if (weatherCode == "Rain" && isDay == 1) {
                    weatherPic.setIcon(setWeatherPhoto("downpour-rainy-day-16531.png"));
                } else if (weatherCode == "Snow" && isDay == 1) {
                    weatherPic.setIcon(setWeatherPhoto("snow-and-blue-cloud-16540.png"));
                } else if (weatherCode == "Snow Showers" && isDay == 1) {
                    weatherPic.setIcon(setWeatherPhoto("blue-moon-and-snowy-night-16543.png"));
                } else if (weatherCode == "Thunder Storm" && isDay == 1) {
                    weatherPic.setIcon(setWeatherPhoto("lightning-and-blue-rain-cloud-16533.png"));
                } else if (weatherCode == "Clear" && isDay == 0) {
                    weatherPic.setIcon(setWeatherPhoto("yellow-moon-16536.png"));
                } else if ((weatherCode == "Cloudy" || weatherCode == "Partly Cloudy") && isDay == 0) {
                    weatherPic.setIcon(setWeatherPhoto("blue-clouds-and-blue-moon-16538.png"));
                } else if ((weatherCode == "Rain" || weatherCode == "Drizzle") && isDay == 0) {
                    weatherPic.setIcon(setWeatherPhoto("rainy-night-and-clouds-with-moon-16539.png"));
                } else if ((weatherCode == "Snow" || weatherCode == "Snow Showers") && isDay == 0) {
                    weatherPic.setIcon(setWeatherPhoto("blue-moon-and-snowy-night-16543.png"));
                }

            }
        }
        
    }

}
